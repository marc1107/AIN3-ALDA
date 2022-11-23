// O. Bittel
// 22.09.2022
package Aufgabenblatt1.dictionary;

import java.util.Iterator;

/**
 * Implementation of the Dictionary interface as AVL tree.
 * <p>
 * The entries are ordered using their natural ordering on the keys,
 * or by a Comparator provided at set creation time, depending on which constructor is used.
 * <p>
 * An iterator for this Aufgabenblatt1.dictionary is implemented by using the parent node reference.
 *
 * @param <K> Key.
 * @param <V> Value.
 */
public class BinaryTreeDictionary<K extends Comparable<? super K>, V> implements Dictionary<K, V> {

    static private class Node<K, V> {
        K key;
        V value;
        int height;
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> parent;

        Node(K k, V v) {
            key = k;
            value = v;
            height = 0;
            left = null;
            right = null;
            parent = null;
        }
    }

    private static class MinEntry<K, V> {
        private K key;
        private V value;
    }

    private Node<K, V> root = null;
    private int size = 0;
    private V oldValue;

    // ...

    @Override
    public V insert(K key, V value) {
        root = insertR(key, value, root);
        if (root != null) {
            root.parent = null;
        }
        return oldValue;
    }

    private Node<K, V> insertR(K key, V value, Node<K, V> p) {
        if (p == null) {
            p = new Node<>(key, value);
            oldValue = null;
            size++;
        } else if (key.compareTo(p.key) < 0) {
            p.left = insertR(key, value, p.left);
            if (p.left != null) {
                p.left.parent = p;
            }
        } else if (key.compareTo(p.key) > 0) {
            p.right = insertR(key, value, p.right);
            if (p.right != null) {
                p.right.parent = p;
            }
        } else {
            oldValue = p.value;
            p.value = value;
        }
        return p;
    }

    @Override
    public V search(K key) {
        return searchR(key, root);
    }

    private V searchR(K key, Node<K, V> p) {
        if (p == null)
            return null;
        else if (key.compareTo(p.key) < 0)
            return searchR(key, p.left);
        else if (key.compareTo(p.key) > 0)
            return searchR(key, p.right);
        else
            return p.value;
    }

    @Override
    public V remove(K key) {
        root = removeR(key, root);
        return oldValue;
    }

    private Node<K, V> removeR(K key, Node<K, V> p) {
        if (p == null) {
            oldValue = null;
        } else if (key.compareTo(p.key) < 0) {
            p.left = removeR(key, p.left);
        } else if (key.compareTo(p.key) > 0) {
            p.right = removeR(key, p.right);
        } else if (p.left == null || p.right == null) {
            oldValue = p.value;
            p = (p.left != null) ? p.left : p.right;
        } else {
            MinEntry<K, V> min = new MinEntry<>();
            p.right = getRemMinR(p.right, min);
            oldValue = p.value;
            p.key = min.key;
            p.value = min.value;
            size--;
        }
        return p;
    }

    private Node<K, V> getRemMinR(Node<K, V> p, MinEntry<K, V> min) {
        assert p != null;
        if (p.left == null) {
            min.key = p.key;
            min.value = p.value;
            p = p.right;
        } else {
            p.left = getRemMinR(p.left, min);
        }
        return p;
    }

    /*private Node<K,V> leftMostDescendant(Node<K,V> p) {
        assert p != null;
        while(p.left != null) {
            p = p.left;
        }
        return p;
    }

    private Node<K,V> parentOfLeftMostAncestor(Node<K,V> p) {
        assert p != null;
        while (p.parent != null && p.parent.right == p) {
            p = p.parent;
        }
        return p.parent;
    }*/

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return null;
    }

    /**
     * Pretty prints the tree
     */
    public void prettyPrint() {
        printR(0, root);
    }

    private void printR(int level, Node<K, V> p) {
        printLevel(level);
        if (p == null) {
            System.out.println("#");
        } else {
            System.out.println(p.key + " " + p.value + "^" + ((p.parent == null) ? "null" : p.parent.key));
            if (p.left != null || p.right != null) {
                printR(level + 1, p.left);
                printR(level + 1, p.right);
            }
        }
    }

    private static void printLevel(int level) {
        if (level == 0) {
            return;
        }
        for (int i = 0; i < level - 1; i++) {
            System.out.print("   ");
        }
        System.out.print("|__");
    }
}
