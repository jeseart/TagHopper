# TagHopper
本Idea插件应用于项目文档和代码链接跳转场景，适用于流程图关联代码、描述文档关联代码，并实现点击跳转的需求。

## 标记示范
- 跳转到puml文件 @puml edrive-prod-corp\doc\核心流程\跳转.puml
- 跳转到文档文件（不区分文件后缀） @doc edrive-prod-corp\doc\详细设计.doc
- 跳转到markdown文件 @md edrive-prod-corp\doc\readme.md
- 跳转到文件夹 @folder edrive-prod-corp\doc
- 跳转到java文件 @java edrive-prod-corp\src\main\java\com\cn21\edrive\Jump.java
- 跳转到java文件指定行 @java edrive-prod-corp\src\main\java\com\cn21\edrive\Jump.java:100
- 跳转到java方法代码引用 @java com.cn21.edrive.Jump#jumpMethod(java.lang.Long)
- 跳转到指定项目的java方法代码引用（项目名是后向匹配的） @java {corp-portal}com.cn21.edrive.Jump#jumpMethod(java.lang.Long)

## 说明
1. @java标记，不标识项目名，则跳转到第一个扫描结果；
2. java方法的引用，可使用右键菜单的Copy / Paste Special->Copy Reference(Ctrl+Alt+Shift+C)复制方法引用；在Javadoc注释中Ctrl+V获得引用路径；
3. 同名类同名不同参的方法在不同模块共存的场景下，可能会出现漏拷贝参数的情况，若无法正确跳转请检查参数个数。
4. 只要被引用的文件和使用本标记的文件在同一个根目录下被导入，即可实现跳转。
5. 示范中使用的相对路径=去掉根目录的绝对路径，项目名均为根目录下的一级目录

## 各类型文件内的注释符号
```
- puml: '
- java: //
- groovy: //
- md：<ul/><!-- --> 或<!-- --><!-- --> (不这么写会有图标飘走的问题)
```

