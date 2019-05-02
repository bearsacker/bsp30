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
package com.guillot.bsp30.tree;

import org.joml.Vector3f;

import com.guillot.bsp30.lumps.Plane;

public class Tree {

    private TreeNode root;

    public Tree(TreeNode root) {
        this.root = root;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public int size() {
        return size(root);
    }

    private int size(TreeNode node) {
        if (node == null) {
            return 0;
        } else {
            return size(node.getLeft()) + 1 + size(node.getRight());
        }
    }

    public int numberNodes() {
        return numberNodes(root);
    }

    private int numberNodes(TreeNode node) {
        if (node == null || node.isLeaf()) {
            return 0;
        } else {
            return numberNodes(node.getLeft()) + 1 + numberNodes(node.getRight());
        }
    }

    public int numberLeaves() {
        return numberLeaves(root);
    }

    private int numberLeaves(TreeNode node) {
        if (node == null) {
            return 0;
        } else if (node.isLeaf()) {
            return 1;
        } else {
            return numberLeaves(node.getLeft()) + numberLeaves(node.getRight());
        }
    }

    public TreeNode findLeafWithPosition(Vector3f position) {
        return findLeafWithPosition(position, root);
    }

    private TreeNode findLeafWithPosition(Vector3f position, TreeNode node) {
        if (node.isLeaf()) {
            return node;
        }

        if (node.getPlane() != null) {
            if (pointInFrontOfPlane(node.getPlane(), position)) {
                return findLeafWithPosition(position, node.getRight());
            } else {
                return findLeafWithPosition(position, node.getLeft());
            }
        }

        return null;
    }

    public boolean pointInFrontOfPlane(Plane plane, Vector3f point) {
        Vector3f normal = new Vector3f(plane.getNormal());
        return (normal.dot(point) - plane.getDist() / 128.0f) >= 0;
    }
}
