package com.example.milk;

import com.example.milk.utils.DateUtil;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Solution {

    public List<List<Integer>> shiftGrid(int[][] grid, int k) {
        int m = grid.length,n=grid[0].length;
        while (k > 0){
            int[][] nums = new int[m][n];
            //遍历整个数组
            for(int i = 0; i < m; i++){
                for(int j = 0; j < n; j++){
                    //第三种情况
                    if(i == m - 1 && j == n - 1){
                        nums[0][0] = grid[i][j];
                        continue;
                    }
                    //第二种那个情况
                    if(i != m - 1 && j == n - 1){
                        nums[i + 1][0] = grid[i][j];
                        continue;
                    }
                    //第一种情况
                    nums[i][j + 1] = grid[i][j];
                }
            }
            grid = nums;
            k--; //迁移次数减一
        }
        List<List<Integer>> result = new ArrayList<>();
        for (int[] ints : grid) {
            List<Integer> list = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                list.add(ints[j]);
            }
            result.add(list);
        }
        return result;
    }

    public static String formatWithPattern(@Nullable Date date, String pattern) {
        if (Objects.isNull(date)) {
            return null;
        }
        return DateUtil.format(date, pattern);
    }

    public static Date parseWithPattern(String dateStr, String pattern) {
        return DateUtil.parse(dateStr, pattern);
    }

    public static void main(String[] args) {

    }

}