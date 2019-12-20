package com.boxuegu.bean;

public class ExercisesBean {
    public int id;//每章习题id
    public String title;//每章习题id标题
    public String content;//每章习题数目
    public int background;//每章习题前边的序号背景
    public int subjectId;//每章习题的id
    public String subject;//每道习题的题干
    public String a;//每道题的A选项
    public String b;//每道题的A选项
    public String c;//每道题的A选项
    public String d;//每道题的A选项
    public int answer;//每道题的A选项
    public int select;//0代表所选的是对的；1，2，3，4分别代表A，B，C，D是错的
}
