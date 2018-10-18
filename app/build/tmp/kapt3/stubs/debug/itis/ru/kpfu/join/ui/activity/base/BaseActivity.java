package itis.ru.kpfu.join.ui.activity.base;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 11}, bv = {1, 0, 2}, k = 1, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b&\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0007\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\bJ\u0012\u0010\u0016\u001a\u00020\u00142\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0014J\u0012\u0010\u0019\u001a\u00020\b2\b\u0010\u000b\u001a\u0004\u0018\u00010\u001aH\u0016J\u0012\u0010\u001b\u001a\u00020\b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001dH\u0016J\u0015\u0010\u001e\u001a\u00020\u00142\b\u0010\u001f\u001a\u0004\u0018\u00010\u0004\u00a2\u0006\u0002\u0010 R\u0012\u0010\u0003\u001a\u00020\u0004X\u00a4\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0012\u0010\u0007\u001a\u00020\bX\u00a4\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0014\u0010\u000b\u001a\u0004\u0018\u00010\u0004X\u00a4\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0014\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u00a4\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0014\u0010\u0012\u001a\u0004\u0018\u00010\u0004X\u00a4\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0013\u0010\r\u00a8\u0006!"}, d2 = {"Litis/ru/kpfu/join/ui/activity/base/BaseActivity;", "Lcom/arellomobile/mvp/MvpAppCompatActivity;", "()V", "contentLayout", "", "getContentLayout", "()I", "enableBackPressed", "", "getEnableBackPressed", "()Z", "menu", "getMenu", "()Ljava/lang/Integer;", "toolbar", "Landroid/support/v7/widget/Toolbar;", "getToolbar", "()Landroid/support/v7/widget/Toolbar;", "toolbarTitle", "getToolbarTitle", "", "enable", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateOptionsMenu", "Landroid/view/Menu;", "onOptionsItemSelected", "item", "Landroid/view/MenuItem;", "setToolbarTitle", "title", "(Ljava/lang/Integer;)V", "app_debug"})
public abstract class BaseActivity extends com.arellomobile.mvp.MvpAppCompatActivity {
    private java.util.HashMap _$_findViewCache;
    
    protected abstract int getContentLayout();
    
    @org.jetbrains.annotations.Nullable()
    protected abstract java.lang.Integer getMenu();
    
    @org.jetbrains.annotations.Nullable()
    protected abstract java.lang.Integer getToolbarTitle();
    
    protected abstract boolean getEnableBackPressed();
    
    @org.jetbrains.annotations.Nullable()
    protected abstract android.support.v7.widget.Toolbar getToolbar();
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    public final void enableBackPressed(boolean enable) {
    }
    
    public final void setToolbarTitle(@org.jetbrains.annotations.Nullable()
    java.lang.Integer title) {
    }
    
    @java.lang.Override()
    public boolean onCreateOptionsMenu(@org.jetbrains.annotations.Nullable()
    android.view.Menu menu) {
        return false;
    }
    
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.Nullable()
    android.view.MenuItem item) {
        return false;
    }
    
    public BaseActivity() {
        super();
    }
}