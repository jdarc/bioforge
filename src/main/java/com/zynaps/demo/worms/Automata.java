package com.zynaps.demo.worms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

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

        for (var i = 0; i < 5; i++) {
            smoothMap();
        }

        drawCircle(new Coordinate(width >> 1, height >> 1), 5);

        processMap();

        var borderSize = 1;
        var borderedMap = new int[width + borderSize * 2][height + borderSize * 2];

        for (var x = 0; x < borderedMap.length; x++) {
            for (var y = 0; y < borderedMap[0].length; y++) {
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
        var wallRegions = getRegions(1);
        var wallThresholdSize = 50;

        wallRegions.stream().filter(wallRegion -> wallRegion.size() < wallThresholdSize).forEach(wallRegion -> {
            for (var tile : wallRegion) {
                map[tile.tileX][tile.tileY] = 0;
            }
        });

        var roomRegions = getRegions(0);
        var roomThresholdSize = 50;
        List<Room> survivingRooms = new ArrayList<>();

        for (var roomRegion : roomRegions) {
            if (roomRegion.size() < roomThresholdSize) {
                for (var tile : roomRegion) {
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
            for (var room : allRooms) {
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

        var bestDistance = 0;
        var bestTileA = new Coordinate(0, 0);
        var bestTileB = new Coordinate(0, 0);
        var bestRoomA = new Room();
        var bestRoomB = new Room();
        var possibleConnectionFound = false;

        for (var roomA : roomListA) {
            if (!forceAccessibilityFromMainRoom) {
                possibleConnectionFound = false;
                if (roomA.connectedRooms.size() > 0) {
                    continue;
                }
            }

            for (var roomB : roomListB) {
                if (roomA == roomB || roomA.IsConnected(roomB)) {
                    continue;
                }

                for (var tileIndexA = 0; tileIndexA < roomA.edgeTiles.size(); tileIndexA++) {
                    for (var tileIndexB = 0; tileIndexB < roomB.edgeTiles.size(); tileIndexB++) {
                        var tileA = roomA.edgeTiles.get(tileIndexA);
                        var tileB = roomB.edgeTiles.get(tileIndexB);
                        var distanceBetweenRooms = (int) (Math.pow(tileA.tileX - tileB.tileX, 2) + Math.pow(tileA.tileY - tileB.tileY, 2));

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

    private void createPassage(Room roomA, Room roomB, Coordinate tileA, Coordinate tileB) {
        Room.ConnectRooms(roomA, roomB);
        var line = getLine(tileA, tileB);
        for (var c : line) {
            drawCircle(c, 5);
        }
    }

    private void drawCircle(Coordinate c, int r) {
        for (var x = -r; x <= r; x++) {
            for (var y = -r; y <= r; y++) {
                if (x * x + y * y <= r * r) {
                    var drawX = c.tileX + x;
                    var drawY = c.tileY + y;
                    if (isInMapRange(drawX, drawY)) {
                        map[drawX][drawY] = 0;
                    }
                }
            }
        }
    }

    private List<Coordinate> getLine(Coordinate from, Coordinate to) {
        List<Coordinate> line = new ArrayList<>();

        var x = from.tileX;
        var y = from.tileY;

        var dx = to.tileX - from.tileX;
        var dy = to.tileY - from.tileY;

        var inverted = false;
        var step = (int) Math.signum(dx);
        var gradientStep = (int) Math.signum(dy);

        var longest = Math.abs(dx);
        var shortest = Math.abs(dy);

        if (longest < shortest) {
            inverted = true;
            longest = Math.abs(dy);
            shortest = Math.abs(dx);

            step = (int) Math.signum(dy);
            gradientStep = (int) Math.signum(dx);
        }

        var gradientAccumulation = longest / 2;
        for (var i = 0; i < longest; i++) {
            line.add(new Coordinate(x, y));

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

    private List<List<Coordinate>> getRegions(int tileType) {
        List<List<Coordinate>> regions = new ArrayList<>();
        var mapFlags = new int[width][height];

        for (var x = 0; x < width; x++) {
            for (var y = 0; y < height; y++) {
                if (mapFlags[x][y] == 0 && map[x][y] == tileType) {
                    var newRegion = getRegionTiles(x, y);
                    regions.add(newRegion);

                    for (var tile : newRegion) {
                        mapFlags[tile.tileX][tile.tileY] = 1;
                    }
                }
            }
        }

        return regions;
    }

    private List<Coordinate> getRegionTiles(int startX, int startY) {
        List<Coordinate> tiles = new ArrayList<>();
        var mapFlags = new int[width][height];
        var tileType = map[startX][startY];

        Queue<Coordinate> queue = new LinkedList<>();
        queue.add(new Coordinate(startX, startY));
        mapFlags[startX][startY] = 1;

        while (queue.size() > 0) {
            var tile = queue.remove();
            tiles.add(tile);

            for (var x = tile.tileX - 1; x <= tile.tileX + 1; x++) {
                for (var y = tile.tileY - 1; y <= tile.tileY + 1; y++) {
                    if (isInMapRange(x, y) && (y == tile.tileY || x == tile.tileX)) {
                        if (mapFlags[x][y] == 0 && map[x][y] == tileType) {
                            mapFlags[x][y] = 1;
                            queue.add(new Coordinate(x, y));
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

        var pseudoRandom = new Random(seed.hashCode());
        for (var x = 0; x < width; x++) {
            for (var y = 0; y < height; y++) {
                if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
                    map[x][y] = 1;
                } else {
                    map[x][y] = (pseudoRandom.nextInt(100) < randomFillPercent) ? 1 : 0;
                }
            }
        }
    }

    private void smoothMap() {
        for (var x = 0; x < width; x++) {
            for (var y = 0; y < height; y++) {
                var neighbourWallTiles = getSurroundingWallCount(x, y);
                if (neighbourWallTiles > 4) {
                    map[x][y] = 1;
                } else if (neighbourWallTiles < 4) {
                    map[x][y] = 0;
                }
            }
        }
    }

    private int getSurroundingWallCount(int gridX, int gridY) {
        var wallCount = 0;
        for (var neighbourX = gridX - 1; neighbourX <= gridX + 1; neighbourX++) {
            for (var neighbourY = gridY - 1; neighbourY <= gridY + 1; neighbourY++) {
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

    private static class Room implements Comparable<Room> {

        public List<Coordinate> tiles;
        public List<Coordinate> edgeTiles;
        public List<Room> connectedRooms;
        public int roomSize;
        public boolean isAccessibleFromMainRoom;
        public boolean isMainRoom;

        public Room() {
        }

        public Room(List<Coordinate> roomTiles, int[][] map) {
            tiles = roomTiles;
            roomSize = tiles.size();
            connectedRooms = new ArrayList<>();

            edgeTiles = new ArrayList<>();
            for (var tile : tiles) {
                for (var x = tile.tileX - 1; x <= tile.tileX + 1; x++) {
                    for (var y = tile.tileY - 1; y <= tile.tileY + 1; y++) {
                        if (x == tile.tileX || y == tile.tileY) {
                            if (map[x][y] == 1) {
                                edgeTiles.add(tile);
                            }
                        }
                    }
                }
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

        public void SetAccessibleFromMainRoom() {
            if (!isAccessibleFromMainRoom) {
                isAccessibleFromMainRoom = true;
                connectedRooms.forEach(Room::SetAccessibleFromMainRoom);
            }
        }

        public boolean IsConnected(Room otherRoom) {
            return connectedRooms.contains(otherRoom);
        }

        @Override
        public int compareTo(Room otherRoom) {
            return Integer.compare(otherRoom.roomSize, roomSize);
        }
    }

    private static class Coordinate {

        public final int tileX;
        public final int tileY;

        public Coordinate(int x, int y) {
            tileX = x;
            tileY = y;
        }
    }
}
