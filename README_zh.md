# ResourceBundle Editor

用于编辑 Java 资源包的 Eclipse 插件。可让您在一个屏幕中管理所有本地化的 .properties 文件。部分功能：排序的键、缺少键/值的警告图标、与 Unicode 之间的转换、键的层次结构视图等。

## Use

<img src="readme/main-screen.png">

请访问 ResourceBundle Editor 网站获取更多屏幕截图和其他信息: http://essiembre.github.io/eclipse-rbe/

## History

Fork of [eclipse-rbe](https://github.com/essiembre/eclipse-rbe) on Github.

## Build

本项目使用 [Tycho](https://github.com/eclipse-tycho/tycho) 与 [Maven](https://maven.apache.org/) 构建，需要 Maven 3.9.0 或更高版本。

开发构建：

```
mvn clean verify
```

发布构建：

```
mvn clean org.eclipse.tycho:tycho-versions-plugin:set-version -DnewVersion=2.0.0 verify
```

## 安装

1. 在eclipse中添加更新站点 `https://raw.githubusercontent.com/tlcsdm/eclipse-rbe/update_site/`
2. 从 [Jenkins](https://jenkins.tlcsdm.com/job/eclipse-plugin/job/eclipse-rbe) 获取
3. <table style="border: none;">
  <tbody>
    <tr style="border:none;">
      <td style="vertical-align: middle; padding-top: 10px; border: none;">
        <a href='http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=7501942' title='Drag and drop into a running Eclipse Indigo workspace to install eclipse-translation'> 
          <img src='https://marketplace.eclipse.org/modules/custom/eclipsefdn/eclipsefdn_marketplace/images/btn-install.svg'/>
        </a>
      </td>
      <td style="vertical-align: middle; text-align: left; border: none;">
        ← Drag it to your eclipse workbench to install! (I recommand Main Toolbar as Drop Target)
      </td>
    </tr>
  </tbody>
</table>
