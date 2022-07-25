package cn.com.kun.component.sentinel.sentinelFlowMonitor;

import com.alibaba.csp.sentinel.Constants;
import com.alibaba.csp.sentinel.command.vo.NodeVo;
import com.alibaba.csp.sentinel.node.DefaultNode;
import com.alibaba.csp.sentinel.node.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 解析jsonTree,只获取感兴趣的resource对应的统计信息
 * 参考源类com.alibaba.csp.sentinel.command.handler.FetchJsonTreeCommandHandler
 *
 * author:xuyaokun_kzx
 * date:2021/9/30
 * desc:
*/
public class CustomFetchJsonTreeHandler {

    static DefaultNode SENTINEL_DEFAULT_CONTEXT_NODE = null;
    static Map<String, DefaultNode> NODE_MAP = new HashMap<>();

    public static List<NodeVo> handle(){

        List<NodeVo> results = new ArrayList<NodeVo>();
        visit(Constants.ROOT, results, null);
        return results;
    }

    /**
     * 为了提高获取统计信息的效率，只获取sentinel_default_context相关的
     * 不是通过接口方式分类的，是通过自定义resourceName的，就会被归类到sentinel_default_context
     * @return
     */
    public static List<NodeVo> getJsonTreeForSentinelDefaultContext(){

        if (SENTINEL_DEFAULT_CONTEXT_NODE == null){
            //一开始为空，待初始化
            AtomicReference<DefaultNode> sentinelDefaultContextNode = new AtomicReference<>();
            Constants.ROOT.getChildList().forEach(node -> {
                DefaultNode defaultNode = (DefaultNode) node;
                if ("sentinel_default_context".equals(defaultNode.getId().getName())){
                    sentinelDefaultContextNode.set(defaultNode);
                }
            });
            if (sentinelDefaultContextNode.get() != null){
                SENTINEL_DEFAULT_CONTEXT_NODE = sentinelDefaultContextNode.get();
            }else {
                //假如jar包里的常量后续改名字，这里就会有问题
                throw new RuntimeException("sentinel_default_context获取失败");
            }
        }
        List<NodeVo> results = new ArrayList<NodeVo>();
        visit(SENTINEL_DEFAULT_CONTEXT_NODE, results, null);
        return results;
    }

    public static List<NodeVo> getJsonTreeForFixedContext(String contextName){

        DefaultNode parentNode = null;
        parentNode = NODE_MAP.get(contextName);
        if (parentNode == null){
            AtomicReference<DefaultNode> contextNodeAtomicReference = new AtomicReference<>();
            Constants.ROOT.getChildList().forEach(node -> {
                DefaultNode defaultNode = (DefaultNode) node;
                if (contextName.equals(defaultNode.getId().getName())){
                    contextNodeAtomicReference.set(defaultNode);
                }
            });
            if (contextNodeAtomicReference.get() != null){
                parentNode = contextNodeAtomicReference.get();
                NODE_MAP.put(contextName, parentNode);
            }else {
                //假如jar包里的常量后续改名字，这里就会有问题
//                throw new RuntimeException(contextName + "获取失败");
                //不要直接抛异常，返回空，让上层判断，假如非要抛出异常，应该在方法上指明抛出啥异常，让上层代码显示捕获
                return null;
            }
        }

        List<NodeVo> results = new ArrayList<NodeVo>();
        visit(parentNode, results, null);
        return results;
    }


    /**
     * Preorder traversal.
     */
    private static void visit(DefaultNode node, List<NodeVo> results, String parentId) {
        NodeVo vo = NodeVo.fromDefaultNode(node, parentId);
        if (!vo.getResource().equals("sentinel_default_context")){
            //跳过sentinel_default_context 不返回
            //因为sentinel_default_context记录的统计信息是其所有子节点的合计，包括了所有业务
            //往往我们不需要，可以跳过这个sentinel_default_context
            results.add(vo);
        }
        String id = vo.getId();
        for (Node n : node.getChildList()) {
            visit((DefaultNode)n, results, id);
        }
    }

}
