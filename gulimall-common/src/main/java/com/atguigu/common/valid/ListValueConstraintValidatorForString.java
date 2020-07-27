package com.atguigu.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 *  @Description @ListValue<String>处理类
 *  @author huaguoguo
 *  @Date 2020/5/23
 */
public class ListValueConstraintValidatorForString implements ConstraintValidator<ListValue,String> {
    Set<String> set = new HashSet<>();

    /**
     * 初始化A-Z的字母
     * @param constraintAnnotation
     */
    @Override
    public void initialize(ListValue constraintAnnotation) {
        for (int i = 65; i < 91; i++) {
            set.add((String.valueOf((char) i)));
        }
    }

    //判断是否校验成功

    /**
     *
     * @param value 需要校验的值
     * @param context
     * @return
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return set.contains(value.toUpperCase());
    }

}
