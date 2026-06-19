package com.example.block_blast;

public class ShapeTemplates {

    public static  int[][] L = {
            {1, 0,0},
            {1, 0,0},
            {1, 1,0}
    };
    public static  int[][] SQUARE = {
            {1, 1,0},
            {1, 1,0},
            {0, 0,0}
    };
    public static  int[][] LINE = {
            {1, 1,1},
            {0, 0,0},
            {0, 0,0}


    };
    public static  int[][] BIGSQUARE = {
            {1,1,1},
            {1,1,1},
            {1,1,1}


    };
    public static  int[][] BIG_L = {
            {1,0,0},
            {1,0,0},
            {1,1,1}


    };
    public static  int[][] BIG_L2 = {
            {1,1,1},
            {0,0,1},
            {0,0,1}


    };
    public static  int[][] L2 = {
            {1,1,0},
            {1,0,0},
            {0,0,0}


    };
    public static  int[][] L3 = {
            {0,1,1},
            {0,0,1},
            {0,0,0}


    };
    public static int[][] J_SHAPE = {
            {0, 0, 1},
            {0, 0, 1},
            {0, 1, 1}
    };
    public static int[][] L4 = {
            {1, 0, 0},
            {1, 1, 1},
            {0, 0, 0}
    };


    public static int[][][] ALL = {L, SQUARE, LINE,BIGSQUARE,BIG_L,BIG_L2,L2,L3,J_SHAPE,L4};
}

