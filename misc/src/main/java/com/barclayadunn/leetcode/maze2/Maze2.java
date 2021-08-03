package com.barclayadunn.leetcode.maze2;

import java.util.*;

public class Maze2 {
    Map<Vertex, List<Vertex>> edgesMap = new HashMap<>();
    List<LinkedList<Vertex>> paths = new ArrayList<>();

    public int shortestDistance(int[][] maze, int[] start, int[] destination) {
        graphAllEdges(maze);
        breadthFirstTraversal(start, destination);
        return paths.get(0).size();
    }

    // maze.length is # of rows, maze[0].length is number of cols
    // [[0,0,1,0,0],[0,0,0,0,0],[0,0,0,1,0],[1,1,0,1,1],[0,0,0,0,0]], start = [0,4], destination = [4,4]
    // 0 0 1 0 S
    // 0 0 0 0 0
    // 0 0 0 1 0
    // 1 1 0 1 1
    // 0 0 0 0 D

    private void graphAllEdges(int[][] maze) {
        List<Vertex> edges;
        // for each x, for each Y, look at each neighbor
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == 0) {
                    final Vertex me = new Vertex(i, j);
                    // neighbor up = x,y-1, if neighbor is zero
                    if (j - 1 >= 0 && maze[i][j - 1] == 0) {
                        // peek at next, if 0 keep rolling
                        if (j - 2 < 0 || maze[i][j - 2] == 1) {
                            edges = edgesMap.getOrDefault(me, new ArrayList<>());
                            edges.add(new Vertex(i, j - 1));
                        }
                    }
                    // neighbor down = x,y+1, if neighbor is zero
                    if (j + 1 < maze[i].length && maze[i][j + 1] == 0) {
                        // peek at next, if 0 keep rolling
                        if (j + 2 == maze[i].length || maze[i][j + 2] == 1) {
                            edges = edgesMap.getOrDefault(me, new ArrayList<>());
                            edges.add(new Vertex(i, j + 1));
                        }
                    }
                    // neighbor left = x-1,y, if neighbor is zero
                    if (i - 1 >= 0 && maze[i - 1][j] == 0) {
                        // peek at next, if 0 keep rolling
                        if (i - 2 < 0 || maze[i - 2][j] == 1) {
                            edges = edgesMap.getOrDefault(me, new ArrayList<>());
                            edges.add(new Vertex(i - 1, j));
                        }
                    }
                    // neighbor right = x+1,y, if neighbor is zero
                    if (i + 1 < maze.length && maze[i + 1][j] == 0) {
                        // peek at next, if 0 keep rolling
                        if (i + 2 == maze.length || maze[i + 2][j] == 1) {
                            edges = edgesMap.getOrDefault(me, new ArrayList<>());
                            edges.add(new Vertex(i + 1, j));
                        }
                    }
                }
            }
        }
    }

    private void breadthFirstTraversal(int[] start, int[] end) {
        Vertex startVertex = boxVertex(start);
        Vertex endVertex = boxVertex(end);

        LinkedList<Vertex> queue = new LinkedList<>();
        HashMap<Vertex, Vertex> visited = new HashMap<>();

        visited.put(startVertex, null);

        Vertex current = startVertex; // the current vertex to check
        while (current != endVertex) { // repeats until the end is reached

            List<Vertex> adjacents = edgesMap.get(current); // get adjacents

            for (Vertex adjnt : adjacents) {
                // add all the adjacents
                if (!visited.containsKey(adjnt)) { // but only if it hasn't already been traversed
                    visited.put(adjnt, current);
                    queue.add(adjnt);
                }
            }

            current = queue.remove(); // goes to the next vertex
        }

        // create the path
        LinkedList<Vertex> path = new LinkedList<>();
        path.addFirst(current);
        while (current != startVertex) {
            current = visited.get(current);
            path.addFirst(current); // addFirst is used to get the correct order
        }
        paths.add(path);

    }

    // start point
    // get edges from point
    // for each of those edges, get edges from endpoint -> LinkedList{point, point, point}



    private Vertex boxVertex(int[] primitive) {
        return new Vertex(primitive[0], primitive[1]);
    }

    class Vertex {
        int x;
        int y;

        private LinkedList<Vertex> adjacents;

        public Vertex(int x, int y) {
            this.x = x;
            this.y = y;
            this.adjacents = new LinkedList<>();
        }

        public void addAdjacent(Vertex adjacentVertex) {
            adjacents.add(adjacentVertex);
        }

        public LinkedList<Vertex> getAdjacents() {
            return this.adjacents;
        }
    }
}