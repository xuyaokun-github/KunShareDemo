package cn.com.kun.springframework.core.resolvableType;

/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.springframework.lang.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 类名随便叫，没啥意义
 *
 * author:xuyaokun_kzx
 * date:2020/6/16
 * desc:
 */
public abstract class FooObj<T> {

    private final Type type;

    protected FooObj() {
        //获取当前new实例的类对象，因为FooObj是一个抽象类
        //这里获取到的将是使用FooObj的运行时那个类对象
        Class<?> clazz = getClass();
        //然后找FooObj类型，获取直接父类型就是FooObj类型类型
        Class<?> fooObjSubclass = findFooObjSubclass(clazz);
        //通过FooObj类型的子类型找到FooObj类型，其实这个和类的运行机制有关？
        Type type = fooObjSubclass.getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        //这里取的第一个元素，就是类名后定义的第一个泛型参数T
        this.type = actualTypeArguments[0];
    }

    private FooObj(Type type) {
        this.type = type;
    }


    public Type getType() {
        return this.type;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return (this == other || (other instanceof FooObj &&
                this.type.equals(((FooObj<?>) other).type)));
    }

    @Override
    public int hashCode() {
        return this.type.hashCode();
    }

    @Override
    public String toString() {
        return "FooObj<" + this.type + ">";
    }

    public static <T> FooObj<T> forType(Type type) {
        return new FooObj<T>(type) {
        };
    }

    /**
     * 一直递归找到FooObj的子类型
     * （为何要这样递归，因为可能存在多重内部类这种情况也能支持）
     * @param child
     * @return
     */
    private static Class<?> findFooObjSubclass(Class<?> child) {
        //获取直接父类
        Class<?> parent = child.getSuperclass();
        if (Object.class == parent) {
            throw new IllegalStateException("Expected FooObj superclass");
        }
        else if (FooObj.class == parent) {
            return child;
        }
        else {
            return findFooObjSubclass(parent);
        }
    }

    public static void main(String[] args) {
        System.out.println();
    }
}
