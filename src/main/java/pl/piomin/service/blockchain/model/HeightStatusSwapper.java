package pl.piomin.service.blockchain.model;

/**
 * @author: HuShili
 * @date: 2019/4/9
 * @description: none
 */

public class HeightStatusSwapper {
    private int height;
    private int[] recent;

    public HeightStatusSwapper(int height, int[] recent) {
        this.height = height;
        this.recent = recent;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int[] getRecent() {
        return recent;
    }

    public void setRecent(int[] recent) {
        this.recent = recent;
    }
}
