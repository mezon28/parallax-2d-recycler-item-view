#Parallax 2d Recycler item view

A ViewGroup when put inside RecyclerView will be a parallax 2D view.

All child will be a layer and slide **vertically** over others when scroll RecyclerView.

##Getting started

1. Copy attribute from attrs.xml to your project

```
<resources>
    <declare-styleable name="Parallax2dView">
        <attr name="transition_intensity" format="float" />
        <attr name="transition_ratio" format="float" />
    </declare-styleable>
</resources>
```

2. Add `Parallax2dView` into your RecyclerView same way as other ViewGroup

```
<motaro222.miy.parallax2drecycler.Parallax2dView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:parallax="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="200dp"
    android:background="@color/colorPrimary"
    android:padding="15dp"
    parallax:transition_intensity="1"
    parallax:transition_ratio="0.25">

    <!-- Will slide over layer2 -->
    <ImageView
        android:id="@+id/layer1"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:contentDescription="@string/app_name"
        android:scaleType="centerInside"/>

    <!-- Will not slide -->
    <FrameLayout
        android:id="@+id/layer2"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        <ImageView
            android:layout_gravity="center"
            android:layout_width="24dp"
            android:layout_height="24dp"
            android:scaleType="fitXY"
            android:contentDescription="@string/app_name"
            android:src="@drawable/bird"/>
    </FrameLayout>
</motaro222.miy.parallax2drecycler.Parallax2dView>
```
