package org.mvnsearch.intellij.plugin.zookeeper.vfs;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.openapi.vfs.VirtualFileSystem;
import org.apache.curator.framework.CuratorFramework;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mvnsearch.intellij.plugin.zookeeper.ZkApplicationComponent;

import java.io.IOException;

/**
 * zookeeper virtual file system
 *
 * @author linux_china
 */
public class ZkVirtualFileSystem extends VirtualFileSystem {
    public static final String PROTOCOL = "zk";

    public ZkVirtualFileSystem() {
    }

    @NotNull
    public String getProtocol() {
        return PROTOCOL;
    }

    @Nullable
    public VirtualFile findFileByPath(@NotNull @NonNls String path) {
        return new ZkNodeVirtualFile(this, path);
    }

    public void refresh(boolean b) {

    }

    @Nullable
    public VirtualFile refreshAndFindFileByPath(@NotNull String path) {
        return findFileByPath(path);
    }

    public void addVirtualFileListener(@NotNull VirtualFileListener virtualFileListener) {

    }

    public void removeVirtualFileListener(@NotNull VirtualFileListener virtualFileListener) {

    }

    protected void deleteFile(Object o, @NotNull VirtualFile virtualFile) throws IOException {
        try {
            getCurator().delete().forPath(virtualFile.getPath());
        } catch (Exception ignore) {

        }
    }

    protected void moveFile(Object o, @NotNull VirtualFile virtualFile, @NotNull VirtualFile virtualFile2) throws IOException {
        try {
            byte[] content = getCurator().getData().forPath(virtualFile.getPath());
            getCurator().create().forPath(virtualFile2.getPath(), content);
            getCurator().delete().forPath(virtualFile.getPath());
        } catch (Exception ignore) {

        }
    }

    protected void renameFile(Object o, @NotNull VirtualFile virtualFile, @NotNull String name) throws IOException {
        String newFilePath = virtualFile.getPath().substring(0, virtualFile.getPath().indexOf("/")) + "/" + name;
        moveFile(o, virtualFile, new ZkNodeVirtualFile(this, newFilePath));
    }

    protected VirtualFile createChildFile(Object o, @NotNull VirtualFile virtualFile, @NotNull String fileName) throws IOException {
        String filePath = virtualFile.getPath() + "/" + fileName;
        try {
            getCurator().create().forPath(filePath);
        } catch (Exception ignore) {

        }
        return new ZkNodeVirtualFile(this, filePath);
    }

    @NotNull
    protected VirtualFile createChildDirectory(Object o, @NotNull VirtualFile virtualFile, @NotNull String directory) throws IOException {
        String filePath = virtualFile.getPath() + "/" + directory;
        try {
            getCurator().create().forPath(filePath);
        } catch (Exception ignore) {

        }
        return new ZkNodeVirtualFile(this, filePath);
    }

    protected VirtualFile copyFile(Object o, @NotNull VirtualFile virtualFile, @NotNull VirtualFile virtualFile2, @NotNull String s) throws IOException {
        try {
            //todo
        } catch (Exception ignore) {

        }
        return null;
    }

    public boolean isReadOnly() {
        return false;
    }

    public CuratorFramework getCurator() {
        return ZkApplicationComponent.getInstance().getCurator();
    }
}