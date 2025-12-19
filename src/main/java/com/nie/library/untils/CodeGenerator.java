package com.nie.library.untils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.Collections;
import java.util.Scanner;

public class CodeGenerator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. 读取模块名
        System.out.print("请输入模块名（如：library, system等）: ");
        String moduleName = scanner.nextLine();

        // 2. 读取表名
        System.out.print("请输入表名（多个用英文逗号分隔，如：books,users）: ");
        String tableNames = scanner.nextLine();

        // 3. 项目路径
        String projectPath = System.getProperty("user.dir");
        String url = "jdbc:mysql://localhost:3306/library_nie?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false";

        // 4. 执行代码生成
        FastAutoGenerator.create(url, "root", "123456")
                // 全局配置
                .globalConfig(builder -> {
                    builder.author("nie") // 设置作者
                            .outputDir(projectPath + "/src/main/java") // 输出目录
                            .disableOpenDir() // 生成后不打开文件夹
                            .fileOverride() // 覆盖已有文件
                            .commentDate("yyyy-MM-dd"); // 日期格式
                })
                // 包配置
                .packageConfig(builder -> {
                    builder.parent("com.nie") // 父包名
                            .moduleName(moduleName) // 模块名
                            .entity("entity") // 实体类包名
                            .mapper("mapper") // Mapper包名
                            .service("service") // Service包名
                            .serviceImpl("service.impl") // ServiceImpl包名
                            .controller("controller") // Controller包名
                            .pathInfo(Collections.singletonMap(
                                    OutputFile.xml,
                                    projectPath + "/src/main/resources/mapper/" + moduleName
                            )); // XML文件输出目录
                })
                // 策略配置
                .strategyConfig(builder -> {
                    builder.addInclude(tableNames.split(",")) // 设置要生成的表名
                            .addTablePrefix("t_", "sys_") // 过滤表前缀

                            // Entity 策略配置
                            .entityBuilder()
                            .enableLombok() // 启用 Lombok
                            .enableTableFieldAnnotation() // 启用字段注解
                            .enableChainModel() // 链式模型

                            // Controller 策略配置
                            .controllerBuilder()
                            .enableRestStyle() // 开启生成@RestController
                            .enableHyphenStyle() // 开启驼峰转连字符

                            // Service 策略配置
                            .serviceBuilder()
                            .formatServiceFileName("%sService") // 服务接口命名
                            .formatServiceImplFileName("%sServiceImpl") // 服务实现命名

                            // Mapper 策略配置
                            .mapperBuilder()
                            .enableBaseResultMap() // 生成 BaseResultMap
                            .enableBaseColumnList() // 生成 BaseColumnList
                            .formatMapperFileName("%sMapper") // Mapper接口命名
                            .formatXmlFileName("%sMapper"); // XML文件命名
                })
                // 模板配置
                .templateConfig(builder -> {
                    // 可以自定义模板，使用默认模板则不需要配置
                    // builder.controller("/templates/controller.java");
                    // builder.service("/templates/service.java");
                    // builder.serviceImpl("/templates/serviceImpl.java");
                    // builder.entity("/templates/entity.java");
                    // builder.mapper("/templates/mapper.java");
                    // builder.xml("/templates/mapper.xml");
                })
                // 模板引擎
                .templateEngine(new VelocityTemplateEngine())
                // 执行生成
                .execute();

        System.out.println("代码生成完成！");
        scanner.close();
    }
}
