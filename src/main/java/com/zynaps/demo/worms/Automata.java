package com.zynaps.demo.worms;

import java.util.*;

class Automata {

    public final int width;
    public final int height;

    public String seed = "Automata";
    public boolean useRandomSeed;

    public int randomFillPercent = 50;

    private int[][] map;

    public Automata(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int[][] generateMap() {
        map = new int[width][height];
        randomFillMap();

        for (int i = 0; i < 5; i++) {
            smoothMap();
        }

        drawCircle(new Coord(width >> 1, height >> 1), 5);

        processMap();

        int borderSize = 1;
        int[][] borderedMap = new int[width + borderSize * 2][height + borderSize * 2];

        for (int x = 0; x < borderedMap.length; x++) {
            for (int y = 0; y < borderedMap[0].length; y++) {
                if (x >= borderSize && x < width + borderSize && y >= borderSize && y < height + borderSize) {
                    borderedMap[x][y] = map[x - borderSize][y - borderSize];
                } else {
                    borderedMap[x][y] = 1;
                }
            }
        }

        return borderedMap;
    }

    private void processMap() {
        List<List<Coord>> wallRegions = getRegions(1);
        int wallThresholdSize = 50;

        wallRegions.stream().filter(wallRegion -> wallRegion.size() < wallThresholdSize).forEach(wallRegion -> {
            for (Coord tile : wallRegion) {
                map[tile.tileX][tile.tileY] = 0;
            }
        });

        List<List<Coord>> roomRegions = getRegions(0);
        int roomThresholdSize = 50;
        List<Room> survivingRooms = new ArrayList<>();

        for (List<Coord> roomRegion : roomRegions) {
            if (roomRegion.size() < roomThresholdSize) {
                for (Coord tile : roomRegion) {
                    map[tile.tileX][tile.tileY] = 1;
                }
            } else {
                survivingRooms.add(new Room(roomRegion, map));
            }
        }
        Collections.sort(survivingRooms);
        survivingRooms.get(0).isMainRoom = true;
        survivingRooms.get(0).isAccessibleFromMainRoom = true;

        connectClosestRooms(survivingRooms);
    }

    private void connectClosestRooms(List<Room> allRooms) {
        connectClosestRooms(allRooms, false);
    }

    private void connectClosestRooms(List<Room> allRooms, boolean forceAccessibilityFromMainRoom) {
        List<Room> roomListA = new ArrayList<>();
        List<Room> roomListB = new ArrayList<>();

        if (forceAccessibilityFromMainRoom) {
            for (Room room : allRooms) {
                if (room.isAccessibleFromMainRoom) {
                    roomListB.add(room);
                } else {
                    roomListA.add(room);
                }
            }
        } else {
            roomListA = allRooms;
            roomListB = allRooms;
        }

        int bestDistance = 0;
        Coord bestTileA = new Coord(0, 0);
        Coord bestTileB = new Coord(0, 0);
        Room bestRoomA = new Room();
        Room bestRoomB = new Room();
        boolean possibleConnectionFound = false;

        for (Room roomA : roomListA) {
            if (!forceAccessibilityFromMainRoom) {
                possibleConnectionFound = false;
                if (roomA.connectedRooms.size() > 0) {
                    continue;
                }
            }

            for (Room roomB : roomListB) {
                if (roomA == roomB || roomA.IsConnected(roomB)) {
                    continue;
                }

                for (int tileIndexA = 0; tileIndexA < roomA.edgeTiles.size(); tileIndexA++) {
                    for (int tileIndexB = 0; tileIndexB < roomB.edgeTiles.size(); tileIndexB++) {
                        Coord tileA = roomA.edgeTiles.get(tileIndexA);
                        Coord tileB = roomB.edgeTiles.get(tileIndexB);
                        int distanceBetweenRooms = (int)(Math.pow(tileA.tileX - tileB.tileX, 2) + Math.pow(tileA.tileY - tileB.tileY, 2));

                        if (distanceBetweenRooms < bestDistance || !possibleConnectionFound) {
                            bestDistance = distanceBetweenRooms;
                            possibleConnectionFound = true;
                            bestTileA = tileA;
                            bestTileB = tileB;
                            bestRoomA = roomA;
                            bestRoomB = roomB;
                        }
                    }
                }
            }
            if (possibleConnectionFound && !forceAccessibilityFromMainRoom) {
                createPassage(bestRoomA, bestRoomB, bestTileA, bestTileB);
            }
        }

        if (possibleConnectionFound && forceAccessibilityFromMainRoom) {
            createPassage(bestRoomA, bestRoomB, bestTileA, bestTileB);
            connectClosestRooms(allRooms, true);
        }

        if (!forceAccessibilityFromMainRoom) {
            connectClosestRooms(allRooms, true);
        }
    }

    private void createPassage(Room roomA, Room roomB, Coord tileA, Coord tileB) {
        Room.ConnectRooms(roomA, roomB);
        List<Coord> line = getLine(tileA, tileB);
        for (Coord c : line) {
            drawCircle(c, 5);
        }
    }

    private void drawCircle(Coord c, int r) {
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                if (x * x + y * y <= r * r) {
                    int drawX = c.tileX + x;
                    int drawY = c.tileY + y;
                    if (isInMapRange(drawX, drawY)) {
                        map[drawX][drawY] = 0;
                    }
                }
            }
        }
    }

    private List<Coord> getLine(Coord from, Coord to) {
        List<Coord> line = new ArrayList<>();

        int x = from.tileX;
        int y = from.tileY;

        int dx = to.tileX - from.tileX;
        int dy = to.tileY - from.tileY;

        boolean inverted = false;
        int step = (int)Math.signum(dx);
        int gradientStep = (int)Math.signum(dy);

        int longest = Math.abs(dx);
        int shortest = Math.abs(dy);

        if (longest < shortest) {
            inverted = true;
            longest = Math.abs(dy);
            shortest = Math.abs(dx);

            step = (int)Math.signum(dy);
            gradientStep = (int)Math.signum(dx);
        }

        int gradientAccumulation = longest / 2;
        for (int i = 0; i < longest; i++) {
            line.add(new Coord(x, y));

            if (inverted) {
                y += step;
            } else {
                x += step;
            }

            gradientAccumulation += shortest;
            if (gradientAccumulation >= longest) {
                if (inverted) {
                    x += gradientStep;
                } else {
                    y += gradientStep;
                }
                gradientAccumulation -= longest;
            }
        }

        return line;
    }

    private List<List<Coord>> getRegions(int tileType) {
        List<List<Coord>> regions = new ArrayList<>();
        int[][] mapFlags = new int[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (mapFlags[x][y] == 0 && map[x][y] == tileType) {
                    List<Coord> newRegion = getRegionTiles(x, y);
                    regions.add(newRegion);

                    for (Coord tile : newRegion) {
                        mapFlags[tile.tileX][tile.tileY] = 1;
                    }
                }
            }
        }

        return regions;
    }

    private List<Coord> getRegionTiles(int startX, int startY) {
        List<Coord> tiles = new ArrayList<>();
        int[][] mapFlags = new int[width][height];
        int tileType = map[startX][startY];

        Queue<Coord> queue = new LinkedList<>();
        queue.add(new Coord(startX, startY));
        mapFlags[startX][startY] = 1;

        while (queue.size() > 0) {
            Coord tile = queue.remove();
            tiles.add(tile);

            for (int x = tile.tileX - 1; x <= tile.tileX + 1; x++) {
                for (int y = tile.tileY - 1; y <= tile.tileY + 1; y++) {
                    if (isInMapRange(x, y) && (y == tile.tileY || x == tile.tileX)) {
                        if (mapFlags[x][y] == 0 && map[x][y] == tileType) {
                            mapFlags[x][y] = 1;
                            queue.add(new Coord(x, y));
                        }
                    }
                }
            }
        }
        return tiles;
    }

    private boolean isInMapRange(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    private void randomFillMap() {

        if (useRandomSeed) {
            seed = String.valueOf(new Date().getTime());
        }

        Random pseudoRandom = new Random(seed.hashCode());
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
                    map[x][y] = 1;
                } else {
                    map[x][y] = (pseudoRandom.nextInt(100) < randomFillPercent) ? 1 : 0;
                }
            }
        }
    }

    private void smoothMap() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int neighbourWallTiles = getSurroundingWallCount(x, y);

                if (neighbourWallTiles > 4) {
                    map[x][y] = 1;
                } else if (neighbourWallTiles < 4) {
                    map[x][y] = 0;
                }
            }
        }
    }

    private int getSurroundingWallCount(int gridX, int gridY) {
        int wallCount = 0;
        for (int neighbourX = gridX - 1; neighbourX <= gridX + 1; neighbourX++) {
            for (int neighbourY = gridY - 1; neighbourY <= gridY + 1; neighbourY++) {
                if (isInMapRange(neighbourX, neighbourY)) {
                    if (neighbourX != gridX || neighbourY != gridY) {
                        wallCount += map[neighbourX][neighbourY];
                    }
                } else {
                    wallCount++;
                }
            }
        }

        return wallCount;
    }

    private class Coord {

        public int tileX;
        public int tileY;

        public Coord(int x, int y) {
            tileX = x;
            tileY = y;
        }
    }

    private static class Room implements Comparable<Room> {

        public List<Coord> tiles;
        public List<Coord> edgeTiles;
        public List<Room> connectedRooms;
        public int roomSize;
        public boolean isAccessibleFromMainRoom;
        public boolean isMainRoom;

        public Room() {
        }

        public Room(List<Coord> roomTiles, int[][] map) {
            tiles = roomTiles;
            roomSize = tiles.size();
            connectedRooms = new ArrayList<>();

            edgeTiles = new ArrayList<>();
            for (Coord tile : tiles) {
                for (int x = tile.tileX - 1; x <= tile.tileX + 1; x++) {
                    for (int y = tile.tileY - 1; y <= tile.tileY + 1; y++) {
                        if (x == tile.tileX || y == tile.tileY) {
                            if (map[x][y] == 1) {
                                edgeTiles.add(tile);
                            }
                        }
                    }
                }
            }
        }

        public void SetAccessibleFromMainRoom() {
            if (!isAccessibleFromMainRoom) {
                isAccessibleFromMainRoom = true;
                connectedRooms.forEach(Room::SetAccessibleFromMainRoom);
            }
        }

        public static void ConnectRooms(Room roomA, Room roomB) {
            if (roomA.isAccessibleFromMainRoom) {
                roomB.SetAccessibleFromMainRoom();
            } else if (roomB.isAccessibleFromMainRoom) {
                roomA.SetAccessibleFromMainRoom();
            }
            roomA.connectedRooms.add(roomB);
            roomB.connectedRooms.add(roomA);
        }

        public boolean IsConnected(Room otherRoom) {
            return connectedRooms.contains(otherRoom);
        }

        @Override
        public int compareTo(Room otherRoom) {
            return Integer.compare(otherRoom.roomSize, roomSize);
        }
    }
}
