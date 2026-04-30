ResourceBundle Editor
===========

用于编辑 Java 资源包的 Eclipse 插件。可让您在一个屏幕中管理所有本地化的 .properties 文件。部分功能：排序的键、缺少键/值的警告图标、与 Unicode 之间的转换、键的层次结构视图等。


<img src="http://essiembre.github.io/eclipse-rbe/img/screenshots/main-screen.png">

请访问 ResourceBundle Editor 网站获取更多屏幕截图和其他信息：http://essiembre.github.io/eclipse-rbe/


如何安装
--------------

**更新站点：**

使用以下内容在 Eclipse 中创建一个新的更新站点：

* Site name:  ``Tlcsdm ResourceBundle Editor``
* Site URL:   ``https://raw.githubusercontent.com/tlcsdm/eclipse-rbe/update_site/``


构建
-----

本项目使用 [Tycho](https://github.com/eclipse-tycho/tycho) 与 [Maven](https://maven.apache.org/) 构建，需要 Maven 3.9.0 或更高版本。

开发构建：

```
mvn clean verify
```

发布构建：

```
mvn clean org.eclipse.tycho:tycho-versions-plugin:set-version -DnewVersion=2.0.0 verify
```

