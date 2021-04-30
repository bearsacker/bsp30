/*
* Copyright (C) 2019 Pierre Guillot
* This file is part of BSP30 library.
*
* BSP30 library is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* BSP30 library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with Way.  If not, see <https://www.gnu.org/licenses/>.
*/
package com.guillot.bsp30;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.joml.Vector3f;

import com.guillot.bsp30.exception.BSP30ParseException;
import com.guillot.bsp30.lumps.ClipNode;
import com.guillot.bsp30.lumps.Edge;
import com.guillot.bsp30.lumps.Entity;
import com.guillot.bsp30.lumps.Face;
import com.guillot.bsp30.lumps.Header;
import com.guillot.bsp30.lumps.Leaf;
import com.guillot.bsp30.lumps.LumpType;
import com.guillot.bsp30.lumps.Model;
import com.guillot.bsp30.lumps.Node;
import com.guillot.bsp30.lumps.Plane;
import com.guillot.bsp30.lumps.Texture;
import com.guillot.bsp30.lumps.TextureInfo;
import com.guillot.bsp30.tree.Tree;
import com.guillot.bsp30.tree.TreeNode;
import com.guillot.bsp30.utils.BinaryFileReader;

public class BSP30 {

    private boolean loaded = false;

    private Header header;

    private Tree bspTree;

    private HashMap<String, List<Entity>> entities = new HashMap<>();

    private ArrayList<Plane> planes = new ArrayList<>();

    private ArrayList<Texture> textures = new ArrayList<>();

    private ArrayList<Vector3f> vertices = new ArrayList<>();

    private ArrayList<Node> nodes = new ArrayList<>();

    private ArrayList<TextureInfo> textureInfos = new ArrayList<>();

    private ArrayList<Face> faces = new ArrayList<>();

    private ArrayList<ClipNode> clipNodes = new ArrayList<>();

    private ArrayList<Leaf> leaves = new ArrayList<>();

    private ArrayList<Edge> edges = new ArrayList<>();

    private ArrayList<Model> models = new ArrayList<>();

    private ArrayList<Face> markSurfaces = new ArrayList<>();

    private ArrayList<Vector3f> surfEdges = new ArrayList<>();

    private String entitiesData;

    private byte[] lightData;

    private byte[] visData;

    public boolean loadFromFile(String path) {
        try {
            BinaryFileReader file = new BinaryFileReader(new FileInputStream(path));

            // Header
            header = new Header(file);

            // Entities
            file.setOffset(header.getLump(LumpType.LUMP_ENTITIES).getOffset());
            entitiesData = file.readString(header.getLump(LumpType.LUMP_ENTITIES).getLength());
            entities = Entity.parseEntities(entitiesData);

            // Planes
            file.setOffset(header.getLump(LumpType.LUMP_PLANES).getOffset());
            int numPlanes = header.getLump(LumpType.LUMP_PLANES).getLength() / Plane.SIZE;
            for (int i = 0; i < numPlanes; i++) {
                planes.add(new Plane(file));
            }

            // Textures
            file.setOffset(header.getLump(LumpType.LUMP_TEXTURES).getOffset());
            int numTextures = file.readInt();
            ArrayList<Integer> offsetsTextures = new ArrayList<>();
            for (int i = 0; i < numTextures; i++) {
                offsetsTextures.add(header.getLump(LumpType.LUMP_TEXTURES).getOffset() + file.readInt());
            }
            for (Integer offsetTexture : offsetsTextures) {
                file.setOffset(offsetTexture);
                textures.add(new Texture(file));
            }

            // TextureInfos
            file.setOffset(header.getLump(LumpType.LUMP_TEXINFO).getOffset());
            int numTextureInfos = header.getLump(LumpType.LUMP_TEXINFO).getLength() / TextureInfo.SIZE;
            for (int i = 0; i < numTextureInfos; i++) {
                textureInfos.add(new TextureInfo(file, textures));
            }

            // Vertices
            file.setOffset(header.getLump(LumpType.LUMP_VERTICES).getOffset());
            int numVertices = header.getLump(LumpType.LUMP_VERTICES).getLength() / 12;
            for (int i = 0; i < numVertices; i++) {
                vertices.add(new Vector3f(file.readFloat(), file.readFloat(), file.readFloat()));
            }

            // Edges
            file.setOffset(header.getLump(LumpType.LUMP_EDGES).getOffset());
            int numEdges = header.getLump(LumpType.LUMP_EDGES).getLength() / Edge.SIZE;
            for (int i = 0; i < numEdges; i++) {
                edges.add(new Edge(file, vertices));
            }

            // SurfEdges
            file.setOffset(header.getLump(LumpType.LUMP_SURFEDGES).getOffset());
            int numSurfEdges = header.getLump(LumpType.LUMP_SURFEDGES).getLength() / 4;
            for (int i = 0; i < numSurfEdges; i++) {
                int index = file.readInt();
                if (index < 0) {
                    surfEdges.add(edges.get(-index).getSecondVertex());
                } else {
                    surfEdges.add(edges.get(index).getFirstVertex());
                }
            }

            // Lighting
            file.setOffset(header.getLump(LumpType.LUMP_LIGHTING).getOffset());
            int sizeLightData = header.getLump(LumpType.LUMP_LIGHTING).getLength();
            lightData = new byte[sizeLightData];
            for (int i = 0; i < sizeLightData; i++) {
                lightData[i] = file.readByte();
            }

            // Faces
            file.setOffset(header.getLump(LumpType.LUMP_FACES).getOffset());
            int numFaces = header.getLump(LumpType.LUMP_FACES).getLength() / Face.SIZE;
            for (int i = 0; i < numFaces; i++) {
                faces.add(new Face(file, planes, surfEdges, textureInfos, lightData));
            }

            // MarkSurfaces
            file.setOffset(header.getLump(LumpType.LUMP_MARKSURFACES).getOffset());
            int numMarkSurfaces = header.getLump(LumpType.LUMP_MARKSURFACES).getLength() / 2;

            for (int i = 0; i < numMarkSurfaces; i++) {
                markSurfaces.add(faces.get(file.readShort()));
            }

            // Nodes
            file.setOffset(header.getLump(LumpType.LUMP_NODES).getOffset());
            int numNodes = header.getLump(LumpType.LUMP_NODES).getLength() / Node.SIZE;
            for (int i = 0; i < numNodes; i++) {
                nodes.add(new Node(file, planes, faces));
            }

            // ClipNodes
            file.setOffset(header.getLump(LumpType.LUMP_CLIPNODES).getOffset());
            int numClipNodes = header.getLump(LumpType.LUMP_CLIPNODES).getLength() / ClipNode.SIZE;
            for (int i = 0; i < numClipNodes; i++) {
                clipNodes.add(new ClipNode(file, planes));
            }

            // Leaves
            file.setOffset(header.getLump(LumpType.LUMP_LEAVES).getOffset());
            int numLeaves = header.getLump(LumpType.LUMP_LEAVES).getLength() / Leaf.SIZE;
            for (int i = 0; i < numLeaves; i++) {
                leaves.add(new Leaf(file, markSurfaces));
            }

            // Models
            file.setOffset(header.getLump(LumpType.LUMP_MODELS).getOffset());
            int numModels = header.getLump(LumpType.LUMP_MODELS).getLength() / Model.SIZE;
            for (int i = 0; i < numModels; i++) {
                models.add(new Model(file, faces));
            }

            // Visibility
            file.setOffset(header.getLump(LumpType.LUMP_VISIBILITY).getOffset());
            int sizeVisData = header.getLump(LumpType.LUMP_VISIBILITY).getLength();
            visData = new byte[sizeVisData];
            for (int i = 0; i < sizeVisData; i++) {
                visData[i] = file.readByte();
            }

            makeBSPTree();
            loaded = true;
        } catch (IOException | BSP30ParseException e) {
            System.err.println("Error while parsing: " + e.getMessage());
            loaded = false;
        }

        return loaded;
    }

    private void makeBSPTree() {
        ArrayList<TreeNode> treeNodes = new ArrayList<>();
        for (Node node : nodes) {
            TreeNode treeNode = new TreeNode();
            treeNode.setPlane(node.getPlane());
            treeNode.setFaces(node.getFaces());

            treeNodes.add(treeNode);
        }

        ArrayList<TreeNode> treeLeaves = new ArrayList<>();
        for (Leaf leaf : leaves) {
            TreeNode treeLeaf = new TreeNode();
            treeLeaf.setFaces(leaf.getFaces());

            treeLeaves.add(treeLeaf);
        }

        ArrayList<Integer> addedLeaves = new ArrayList<>();
        ArrayList<Integer> addedNodes = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            int indexLeft = nodes.get(i).getChildren()[0];
            int indexRight = nodes.get(i).getChildren()[1];

            if (indexLeft <= 0) {
                indexLeft = -(indexLeft + 1);
                if (!addedLeaves.contains(indexLeft)) {
                    addedLeaves.add(indexLeft);
                }

                treeNodes.get(i).setLeft(treeLeaves.get(indexLeft));
            } else {
                if (!addedNodes.contains(indexLeft)) {
                    addedNodes.add(indexLeft);
                }
                treeNodes.get(i).setLeft(treeNodes.get(indexLeft));
            }

            if (indexRight <= 0) {
                indexRight = -(indexRight + 1);
                if (!addedLeaves.contains(indexRight)) {
                    addedLeaves.add(indexRight);
                }

                treeNodes.get(i).setRight(treeLeaves.get(indexRight));
            } else {
                if (!addedNodes.contains(indexRight)) {
                    addedNodes.add(indexRight);
                }

                treeNodes.get(i).setRight(treeNodes.get(indexRight));
            }
        }

        bspTree = new Tree(treeNodes.get(0));
    }

    public void printBSPFileSizes() {
        printBSPFileSizes(System.out);
    }

    public void printBSPFileSizes(PrintStream outStream) {
        int totalmemory = 0;

        outStream.println("");
        outStream.println("Object names  Objects/Maxobjs  Memory / Maxmem  Fullness");
        outStream.println("------------  ---------------  ---------------  --------");
        totalmemory += arrayUsage(outStream, "models", models.size(), 400, 64);
        totalmemory += arrayUsage(outStream, "planes", planes.size(), 32767, 20);
        totalmemory += arrayUsage(outStream, "vertexes", vertices.size(), 65535, 12);
        totalmemory += arrayUsage(outStream, "nodes", nodes.size(), 32767, 24);
        totalmemory += arrayUsage(outStream, "texinfos", textureInfos.size(), 8192, 40);
        totalmemory += arrayUsage(outStream, "faces", faces.size(), 65535, 20);
        totalmemory += arrayUsage(outStream, "clipnodes", clipNodes.size(), 32767, 8);
        totalmemory += arrayUsage(outStream, "leaves", leaves.size(), 8192, 28);
        totalmemory += arrayUsage(outStream, "marksurfaces", markSurfaces.size(), 65535, 2);
        totalmemory += arrayUsage(outStream, "surfedges", surfEdges.size(), 512000, 4);
        totalmemory += arrayUsage(outStream, "edges", edges.size(), 256000, 4);
        totalmemory += arrayUsage(outStream, "texdata", textures.size(), 512, 40);
        outStream.format("%-12s  %7d/%-7d     [variable]   [variable]    \n", "entities", entities.size(), 1024);
        outStream.println("");
        totalmemory += globUsage(outStream, "entitiesdata", entitiesData != null ? entitiesData.length() : 0, 131072);
        totalmemory += globUsage(outStream, "lightdata", lightData != null ? lightData.length : 0, 2097152);
        totalmemory += globUsage(outStream, "visdata", visData != null ? visData.length : 0, 2097152);
        outStream.println("=== Total BSP file data space used: " + totalmemory + " bytes ===");
    }

    private int arrayUsage(PrintStream outStream, String item, int nbItems, int maxItems, int itemSize) {
        float percentage = (nbItems * 100) / (float) maxItems;

        outStream.format("%-12s  %7d/%-7d  %7d/%-7d  (%4.1f%%)", item, nbItems, maxItems, nbItems * itemSize, maxItems * itemSize,
                percentage);
        if (percentage > 80.0) {
            outStream.print("    VERY FULL!");
        } else if (percentage > 95.0) {
            outStream.print("    SIZE DANGER!");
        } else if (percentage > 99.9) {
            outStream.print("    SIZE OVERFLOW!!!");
        }
        outStream.println("");

        return nbItems * itemSize;
    }

    private int globUsage(PrintStream outStream, String item, int itemStorage, int maxStorage) {
        float percentage = (itemStorage * 100) / (float) maxStorage;

        outStream.format("%-12s     [variable]    %7d/%-7d  (%4.1f%%)", item, itemStorage, maxStorage, percentage);
        if (percentage > 80.0) {
            outStream.print("    VERY FULL!");
        } else if (percentage > 95.0) {
            outStream.print("    SIZE DANGER!");
        } else if (percentage > 99.9) {
            outStream.print("    SIZE OVERFLOW!!!");
        }
        outStream.println("");

        return itemStorage;
    }

    public void printEntities() {
        printEntities(System.out);
    }

    public void printEntities(PrintStream outStream) {
        for (Entry<String, List<Entity>> entry : entities.entrySet()) {
            if (entry.getValue() != null) {
                entry.getValue().forEach(entity -> outStream.println(entity));
            }
        }
    }

    public boolean isLoaded() {
        return loaded;
    }

    public Header getHeader() {
        return header;
    }

    public HashMap<String, List<Entity>> getEntities() {
        return entities;
    }

    public ArrayList<Plane> getPlanes() {
        return planes;
    }

    public ArrayList<Texture> getTextures() {
        return textures;
    }

    public ArrayList<Vector3f> getVertices() {
        return vertices;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<TextureInfo> getTextureInfos() {
        return textureInfos;
    }

    public ArrayList<Face> getFaces() {
        return faces;
    }

    public ArrayList<ClipNode> getClipNodes() {
        return clipNodes;
    }

    public ArrayList<Leaf> getLeaves() {
        return leaves;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public ArrayList<Face> getMarkSurfaces() {
        return markSurfaces;
    }

    public ArrayList<Vector3f> getSurfEdges() {
        return surfEdges;
    }

    public ArrayList<Model> getModels() {
        return models;
    }

    public byte[] getLightData() {
        return lightData;
    }

    public byte[] getVisData() {
        return visData;
    }

    public Tree getBspTree() {
        return bspTree;
    }
}
