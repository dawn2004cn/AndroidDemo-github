package com.noahedu.common.util;

/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：Soduku$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/3/16$ 8:37$
 */
public class Soduku {
    static int[][] data=new int[][]{
            {0,0,5,3,0,0,0,0,0},
            {8,0,0,0,0,0,0,2,0},
            {0,7,0,0,1,0,5,0,0},
            {4,0,0,0,0,5,3,0,0},
            {0,1,0,0,7,0,0,0,6},
            {0,0,3,2,0,0,0,8,0},
            {0,0,0,5,0,0,0,0,9},
            {0,0,4,0,0,0,0,3,0},
            {0,0,0,0,0,0,7,0,0}
    };
    static int[][] data1=new int[][]{
            {5,3,0,0,1,0,0,0,0},
            {0,0,7,5,0,0,3,6,1},
            {2,0,0,0,0,0,5,0,7},
            {0,2,4,0,0,0,0,0,3},
            {0,0,3,9,2,0,0,0,8},
            {8,0,0,3,7,6,4,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,4,0,0,0,0,3,0},
            {7,0,0,0,3,1,0,2,0}
    };
    static char[][] boards= new char[][]{
                 {'8', '.', '.', '.', '.', '.', '.', '.', '.'},
                 {'.', '.', '3', '6', '.', '.', '.', '.', '.'},
                 {'.', '7', '.', '.', '9', '.', '2', '.', '.'},
                 {'.', '5', '.', '.', '.', '7', '.', '.', '.'},
                 {'.', '.', '.', '.', '4', '5', '7', '.', '.'},
                 {'.', '.', '.', '1', '.', '.', '.', '3', '.'},
                 {'.', '.', '1', '.', '.', '.', '.', '6', '8'},
                 {'.', '.', '8', '5', '.', '.', '.', '1', '.'},
                 {'.', '9', '.', '.', '.', '.', '4', '.', '.'}
    };
    static int[][] mark=new int[9][9];
    static int[] rows= new int[9];
    static int[] cols =new int[9];
    static int[] blocks = new int[9];
    static int[] nums = {1,2,3,4,5,6,7,8,9};
    static boolean flag=true;
    static boolean ok=false;
    static int cnt;
    public static void main(String[] args) {
        long t1=System.currentTimeMillis();
        solveSudoku(boards);
        long t2=System.currentTimeMillis();
        System.out.println(t2-t1);

    }

    /**
     * @param i
     * @param j
     * @return 根据行列号获取board 9个方格的数   1 4 6
     *                                         2 5 7
     *                                         3 6 9
     */
    private static int which(int i, int j) {
        if (i <= 2) {
            if (j <= 2)
                return 1;
            if (j <= 5)
                return 2;
            return 3;
        }
        if (i <= 5) {
            if (j <= 2)
                return 4;
            if (j <= 5)
                return 5;
            return 6;
        }
        if (j <= 2)
            return 7;
        if (j <= 5)
            return 8;
        return 9;
    }

    /**
     * @param datas
     */
    public static void solveSudoku(char[][] datas) {
        //方格
        int[][] boards = new int[10][10];
        // 行
        int[][] rows = new int[10][10];
        // 列
        int[][] cols = new int[10][10];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (datas[i][j] != '.') {
                    rows[i][datas[i][j] - '0'] = 1;
                    cols[j][datas[i][j] - '0'] = 1;
                    boards[which(i, j)][datas[i][j] - '0'] = 1;
                }

                System.out.print(datas[i][j]+" ");
            }

            System.out.println();
        }

        dfs(boards, rows, cols, datas, 0, 0);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(datas[i][j]+" ");
            }
            System.out.println();
        }
    }

    /**
     * @param boards
     * @param rows
     * @param cols
     * @param datas
     * @param i
     * @param j
     * @return
     */
    private static boolean dfs(int[][] boards, int[][] rows, int[][] cols, char[][] datas, int i, int j) {
        if (j == 9) return true;
        if (datas[i][j] != '.') {
            if (i + 1 < 9) {
                boolean rst = dfs(boards, rows, cols, datas, i + 1, j);
                if (rst) {
                    return true;
                }
            } else {
                boolean rst = dfs(boards, rows, cols, datas, 0, j + 1);
                if (rst) {
                    return true;
                }
            }
        } else {
            for (int x = 1; x <= 9; x++) {
                if (rows[i][x] != 1 && cols[j][x] != 1 && boards[which(i, j)][x] != 1) {
                    rows[i][x] = 1;
                    cols[j][x] = 1;
                    boards[which(i, j)][x] = 1;
                    datas[i][j] = (char) (x + '0');
                    if (i + 1 < 9) {
                        boolean rst = dfs(boards, rows, cols, datas, i + 1, j);
                        if (rst) {
                            return true;
                        }
                    } else {
                        boolean rst = dfs(boards, rows, cols, datas, 0, j + 1);
                        if (rst) {
                            return true;
                        }
                    }
                    rows[i][x] = 0;
                    cols[j][x] = 0;
                    boards[which(i, j)][x] = 0;
                    datas[i][j] = '.';
                }
            }
        }
        return false;
    }
}

