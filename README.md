# 一款不错的3D版翻页公告

[ ![Download](https://api.bintray.com/packages/a1002326270/maven/marquee3dlibs/images/download.svg?version=1.0.1) ](https://bintray.com/a1002326270/maven/marquee3dlibs/1.0.1/link)

<img src="https://github.com/xiangcman/Marquee3DView-master/blob/master/photos/simple.gif" width="250"/>

### 使用:

- **布局:**
```
<!--指定从下到上翻滚-->
<com.xiangcheng.marquee3dview.Marquee3DView
    android:id="@+id/marquee3DView"
    android:layout_width="wrap_content"
    android:layout_height="25dp"
    android:layout_marginBottom="8dp"
    android:layout_marginEnd="8dp"
    android:layout_marginStart="8dp"
    android:layout_marginTop="8dp"
    app:direction="D2U"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent" />

<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginBottom="8dp"
    android:layout_marginTop="8dp"
    android:background="#FFC0CB"
    android:gravity="center"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintEnd_toEndOf="@+id/marquee3DView"
    app:layout_constraintStart_toStartOf="@+id/marquee3DView"
    app:layout_constraintTop_toBottomOf="@+id/marquee3DView">
    <!--从上到下翻滚-->
    <com.xiangcheng.marquee3dview.Marquee3DView
        android:id="@+id/marquee3DView2"
        android:layout_width="wrap_content"
        android:layout_height="40dp"
        android:layout_marginStart="10dp"
        app:back_color="#00ffffff"
        app:direction="U2D"
        app:highlight_color="#FF6347"
        app:highlight_position="3"
        app:rotate_duration="1500"
        app:show_duration="1500" />
</LinearLayout>
```
- **属性:**
```
<declare-styleable name="Marquee3DView">
    <!--指定旋转的方向-->
    <attr name="direction" format="enum">
        <!--从上到下-->
        <enum name="U2D" value="2" />
        <!--从下到上-->
        <enum name="D2U" value="1" />
    </attr>
    <!--高亮的item位置-->
    <attr name="highlight_position" format="integer" />
    <!--item的颜色-->
    <attr name="back_color" format="color" />
    <!--高亮的文字、下划线颜色-->
    <attr name="highlight_color" format="color" />
    <!--3D旋转的时间-->
    <attr name="rotate_duration" format="integer" />
    <!--停留显示的时间-->
    <attr name="show_duration" format="integer" />
    <!--右边文字的颜色-->
    <attr name="label_text_color" format="color" />
    <!--右边文字的大小-->
    <attr name="label_text_size" format="dimension" />
    <!--指定左边图片的半径-->
    <attr name="label_bitmap_radius" format="dimension" />
    <!--bitmap和text之间的间距-->
    <attr name="label_bitmap_text_offset" format="dimension" />
</declare-styleable>
```
- **代码:**
```
/**
 * 设置显示的label
 * @param marqueeLabels
 */
public void setMarqueeLabels(List<String> marqueeLabels)
```
```
/**
 * 设置显示的bitmap
 * @param labelBitmap
 */
public void setLabelBitmap(List<Bitmap> labelBitmap)
```
```
/**
 * 点击监听
 *
 */
setOnWhereItemClick(new Marquee3DView.OnWhereItemClick() {
    @Override
    public void onItemClick(int position) {
        //TODO
    }
});
```

- **gradle:**
```
compile 'com.xiangcheng:marquee3dlibs:1.0.1'
```
- **maven:**
```
<dependency>
  <groupId>com.xiangcheng</groupId>
  <artifactId>marquee3dlibs</artifactId>
  <version>1.0</version>
  <type>pom</type>
</dependency>
```

**欢迎大家提出问题，留言板留言或邮箱直接联系我。我会第一时间测试相关的bug**

**如果你有更好的效果，或是想修改效果，请与作者联系，谢谢!!!**

**欢迎客官到本店光临(qq群):**

<image src="https://github.com/1002326270xc/LayoutManager-FlowLayout/blob/master/photos/IMG_0221.jpg" width="250" width="250" title="qq群"/>

### thanks:
[Roll3DImageView](https://github.com/zhangyuChen1991/Roll3DImageView)

[简书](https://www.jianshu.com/p/caa5f38d393a)
   



