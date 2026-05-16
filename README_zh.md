# ResourceBundle Editor

用于编辑 Java 资源包的 Eclipse 插件。可让您在一个屏幕中管理所有本地化的 .properties 文件。部分功能：排序的键、缺少键/值的警告图标、与 Unicode 之间的转换、键的层次结构视图等。

## Use

<img src="readme/main-screen.png">

请访问 ResourceBundle Editor 网站获取更多屏幕截图和其他信息: http://essiembre.github.io/eclipse-rbe/

## 功能介绍 / 帮助

ResourceBundle Editor 可以让您将一组 `*.properties` 文件（每个 locale 一个）作为单一资源包进行编辑。

### 编辑器布局

当您打开任意属于资源包的 `*.properties` 文件时，插件会以多页编辑器形式打开：

- **Properties 页**：主编辑页，可在所有语言之间编辑翻译。自 1.2.0 起，提供两种可切换的布局（见下文）。
- **每个 locale 一页的源代码编辑器**：常规的 Eclipse 文本编辑器，可对底层的 `.properties` 文件进行精细编辑或查看实际输出。
- **新建… 页**：用于向资源包中添加新 locale 的页面。

### 视图切换 (1.2.0+)

Properties 页可使用以下两种布局，通过编辑器顶部的切换按钮快速切换：

- **表单视图**（默认）：经典布局，左侧为 key 树，右侧为按语言分块的值编辑区。适合逐个 key 编辑，可使用注释、相似/重复值检测等完整功能。
- **表格视图**：电子表格风格布局，每行为一个 key，每列为一种 locale。适合快速查看缺失翻译并批量编辑单元格。

所选视图模式会被持久化为偏好设置，下次打开 Eclipse 时仍会保留该设置。

### 其他 UI 元素

- **Key 树（表单视图左侧面板）**：支持层次或平铺方式浏览 key。其上方的工具栏提供：新增 key、重命名、复制、删除、注释/取消注释、展开/折叠全部以及切换平铺/层次布局。过滤框可用于搜索 key；*仅显示未完成翻译* 过滤器会隐藏所有翻译都已完成的 key。
- **值编辑器（表单视图右侧面板）**：每种 locale 一个文本区，附带国旗图标、相似/重复值提示，以及 “注释此条目” 复选框。
- **大纲视图（Outline）**：以 Eclipse 大纲形式展示 key 树以便快速导航。
- **首选项** (`Window > Preferences > ResourceBundle Editor`)：控制格式化、key 分组、key 排序、显示哪些 locale、性能/告警选项等。
- **新建 ResourceBundle 向导** (`File > New > Other… > ResourceBundle`)：基于所选 locale 一次性创建包含多个属性文件的资源包。

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
