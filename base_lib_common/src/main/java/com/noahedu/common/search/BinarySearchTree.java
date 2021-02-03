package com.noahedu.common.search;


import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：BinaryTreeSearch$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/4/18$ 17:18$
 */
public class BinarySearchTree implements  IArraySearch {
    @Override
    public int search(int[] sourceArray, int target) throws Exception {
        try {
            BinarySearchTree bst = new BinarySearchTree();
            System.out.println("查找树是否为空？ " + (bst.isEmpty() ? "是" : "否"));
            //int[] keys = new int[] { 15, 6, 18, 3, 7, 13, 20, 2, 9, 4 };
            for (int key : sourceArray) {
                bst.insert(key);
            }
            System.out.println("查找树是否为空？ " + (bst.isEmpty() ? "是" : "否"));
            TreeNode minkeyNode = bst.minElemNode(bst.getRoot());
            System.out.println("最小关键字： " + minkeyNode.getKey());
            testNode(bst, minkeyNode);
            TreeNode maxKeyNode = bst.maxElemNode(bst.getRoot());
            System.out.println("最大关键字： " + maxKeyNode.getKey());
            testNode(bst, maxKeyNode);
            System.out.println("根结点关键字： " + bst.getRoot().getKey());
            testNode(bst, bst.getRoot());
            testTraverse(bst);
            System.out.println("****************************** ");
            testTraverse(bst);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return  -1;
    }
    // 树的根结点
    private TreeNode root = null;

    // 遍历结点列表
    private List<TreeNode> nodelist = new ArrayList<TreeNode>();

    private class TreeNode {

        private int key;
        private TreeNode left;
        private TreeNode right;
        private TreeNode parent;

        public TreeNode(int key, TreeNode left, TreeNode right,
                        TreeNode parent) {
            this.key = key;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }

        public int getKey() {
            return key;
        }

        public String toString() {
            String leftkey = (left == null ? "" : String
                    .valueOf(left.key));
            String rightkey = (right == null ? "" : String
                    .valueOf(right.key));
            return "(" + leftkey + " , " + key + " , " + rightkey + ")";
        }

    }

    /**
     * isEmpty: 判断二叉查找树是否为空；若为空，返回 true ，否则返回 false .
     *
     */
    public boolean isEmpty() {
        if (root == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * TreeEmpty: 对于某些二叉查找树操作(比如删除关键字)来说，若树为空，则抛出异常。
     */
    public void TreeEmpty() throws Exception {
        if (isEmpty()) {
            throw new Exception("树为空!");
        }
    }

    /**
     * search: 在二叉查找树中查询给定关键字
     *
     * @param key
     *            给定关键字
     * @return 匹配给定关键字的树结点
     */
    public TreeNode search(int key) {
        TreeNode pNode = root;
        while (pNode != null && pNode.key != key) {
            if (key < pNode.key) {
                pNode = pNode.left;
            } else {
                pNode = pNode.right;
            }
        }
        return pNode;
    }

    /**
     * minElemNode: 获取二叉查找树中的最小关键字结点
     *
     * @return 二叉查找树的最小关键字结点
     * @throws Exception
     *             若树为空，则抛出异常
     */
    public TreeNode minElemNode(TreeNode node) throws Exception {
        if (node == null) {
            throw new Exception("树为空！");
        }
        TreeNode pNode = node;
        while (pNode.left != null) {
            pNode = pNode.left;
        }
        return pNode;
    }

    /**
     * maxElemNode: 获取二叉查找树中的最大关键字结点
     *
     * @return 二叉查找树的最大关键字结点
     * @throws Exception
     *             若树为空，则抛出异常
     */
    public TreeNode maxElemNode(TreeNode node) throws Exception {
        if (node == null) {
            throw new Exception("树为空！");
        }
        TreeNode pNode = node;
        while (pNode.right != null) {
            pNode = pNode.right;
        }
        return pNode;
    }

    /**
     * successor: 获取给定结点在中序遍历顺序下的后继结点
     *
     * @param node
     *            给定树中的结点
     * @return 若该结点存在中序遍历顺序下的后继结点，则返回其后继结点；否则返回 null
     * @throws Exception
     */
    public TreeNode successor(TreeNode node) throws Exception {
        if (node == null) {
            return null;
        }

        // 若该结点的右子树不为空，则其后继结点就是右子树中的最小关键字结点
        if (node.right != null) {
            return minElemNode(node.right);
        }
        // 若该结点右子树为空
        TreeNode parentNode = node.parent;
        while (parentNode != null && node == parentNode.right) {
            node = parentNode;
            parentNode = parentNode.parent;
        }
        return parentNode;
    }

    /**
     * precessor: 获取给定结点在中序遍历顺序下的前趋结点
     *
     * @param node
     *            给定树中的结点
     * @return 若该结点存在中序遍历顺序下的前趋结点，则返回其前趋结点；否则返回 null
     * @throws Exception
     */
    public TreeNode precessor(TreeNode node) throws Exception {
        if (node == null) {
            return null;
        }

        // 若该结点的左子树不为空，则其前趋结点就是左子树中的最大关键字结点
        if (node.left != null) {
            return maxElemNode(node.left);
        }
        // 若该结点左子树为空
        TreeNode parentNode = node.parent;
        while (parentNode != null && node == parentNode.left) {
            node = parentNode;
            parentNode = parentNode.parent;
        }
        return parentNode;
    }

    /**
     * insert: 将给定关键字插入到二叉查找树中
     *
     * @param key
     *            给定关键字
     */
    public void insert(int key) {
        TreeNode parentNode = null;
        TreeNode newNode = new TreeNode(key, null, null, null);
        TreeNode pNode = root;
        if (root == null) {
            root = newNode;
            return;
        }
        while (pNode != null) {
            parentNode = pNode;
            if (key < pNode.key) {
                pNode = pNode.left;
            } else if (key > pNode.key) {
                pNode = pNode.right;
            } else {
                // 树中已存在匹配给定关键字的结点，则什么都不做直接返回
                return;
            }
        }
        if (key < parentNode.key) {
            parentNode.left = newNode;
            newNode.parent = parentNode;
        } else {
            parentNode.right = newNode;
            newNode.parent = parentNode;
        }

    }

    /**
     * insert: 从二叉查找树中删除匹配给定关键字相应的树结点
     *
     * @param key
     *            给定关键字
     */
    public void delete(int key) throws Exception {
        TreeNode pNode = search(key);
        if (pNode == null) {
            throw new Exception("树中不存在要删除的关键字!");
        }
        delete(pNode);
    }

    /**
     * delete: 从二叉查找树中删除给定的结点.
     *
     * @param pNode
     *            要删除的结点
     *
     *            前置条件： 给定结点在二叉查找树中已经存在
     * @throws Exception
     */
    private void delete(TreeNode pNode) throws Exception {
        if (pNode == null) {
            return;
        }
        if (pNode.left == null && pNode.right == null) { // 该结点既无左孩子结点，也无右孩子结点
            TreeNode parentNode = pNode.parent;
            if (pNode == parentNode.left) {
                parentNode.left = null;
            } else {
                parentNode.right = null;
            }
            return;
        }
        if (pNode.left == null && pNode.right != null) { // 该结点左孩子结点为空，右孩子结点非空
            TreeNode parentNode = pNode.parent;
            if (pNode == parentNode.left) {
                parentNode.left = pNode.right;
                pNode.right.parent = parentNode;
            } else {
                parentNode.right = pNode.right;
                pNode.right.parent = parentNode;
            }
            return;
        }
        if (pNode.left != null && pNode.right == null) { // 该结点左孩子结点非空，右孩子结点为空
            TreeNode parentNode = pNode.parent;
            if (pNode == parentNode.left) {
                parentNode.left = pNode.left;
                pNode.right.parent = parentNode;
            } else {
                parentNode.right = pNode.left;
                pNode.right.parent = parentNode;
            }
            return;
        }
        // 该结点左右孩子结点均非空，则删除该结点的后继结点，并用该后继结点取代该结点
        TreeNode successorNode = successor(pNode);
        delete(successorNode);
        pNode.key = successorNode.key;
    }

    /**
     * inOrderTraverseList: 获得二叉查找树的中序遍历结点列表
     *
     * @return 二叉查找树的中序遍历结点列表
     */
    public List<TreeNode> inOrderTraverseList() {
        if (nodelist != null) {
            nodelist.clear();
        }
        inOrderTraverse(root);
        return nodelist;
    }

    /**
     * inOrderTraverse: 对给定二叉查找树进行中序遍历
     *
     * @param root
     *            给定二叉查找树的根结点
     */
    private void inOrderTraverse(TreeNode root) {
        if (root != null) {
            inOrderTraverse(root.left);
            nodelist.add(root);
            inOrderTraverse(root.right);
        }
    }

    /**
     * toStringOfOrderList: 获取二叉查找树中关键字的有序列表
     *
     * @return 二叉查找树中关键字的有序列表
     */
    public String toStringOfOrderList() {
        StringBuilder sbBuilder = new StringBuilder(" [ ");
        for (TreeNode p : inOrderTraverseList()) {
            sbBuilder.append(p.key);
            sbBuilder.append(" ");
        }
        sbBuilder.append("]");
        return sbBuilder.toString();
    }

    /**
     * 获取该二叉查找树的字符串表示
     */
    public String toString() {
        StringBuilder sbBuilder = new StringBuilder(" [ ");
        for (TreeNode p : inOrderTraverseList()) {
            sbBuilder.append(p);
            sbBuilder.append(" ");
        }
        sbBuilder.append("]");
        return sbBuilder.toString();
    }

    public TreeNode getRoot() {
        return root;
    }

    public static void testNode(BinarySearchTree bst, TreeNode pNode)
            throws Exception {
        System.out.println("本结点: " + pNode);
        System.out.println("前趋结点: " + bst.precessor(pNode));
        System.out.println("后继结点: " + bst.successor(pNode));
    }

    public static void testTraverse(BinarySearchTree bst) {
        System.out.println("二叉树遍历：" + bst);
        System.out.println("二叉查找树转换为有序列表: " + bst.toStringOfOrderList());
    }

      /**
     * 查找指定值的树节点
     * @param value
     * @return
     */

    public TreeNode findNode(int value){
        TreeNode curr = root;
        while (curr.key != value){
            if (curr.key > value){
                curr = curr.left;
            }else curr = curr.right;
        }
        if (curr == null)return null;
        return new TreeNode(curr.key,null,null,null);
    }

    /**
     * 前序遍历（递归）
     * @param node
     */
    public void preOrder(TreeNode node){
        if (node != null){
            System.out.println(node.key);
            preOrder(node.left);
            preOrder(node.right);
        }
    }

    /**
     * 前序遍历（非递归）
     * @param node
     */
    public void preOrder1(TreeNode node){
        Stack<TreeNode> stack = new Stack<>();
        while (node != null || !stack.empty()){
            while (node != null){
                System.out.println(node.key);
                stack.push(node);
                node = node.left;
            }
            if (!stack.empty()){
                node = stack.pop();
                node = node.right;
            }
        }
    }
    /**
     * 中序遍历（递归）
     * @param node
     */
    public void midOrder(TreeNode node){
        if (node != null){
            midOrder(node.left);
            System.out.println(node.key);
            midOrder(node.right);
        }
    }

    /**
     * 中序遍历（非递归）
     * @param node
     */
    public void midOrder1(TreeNode node){

        Stack<TreeNode> stack = new Stack<>();
        while ( node != null || !stack.empty()){
            while ( node != null){
                stack.push(node);
                node = node.left;
            }
            if (!stack.empty()){
                node = stack.pop();
                System.out.println(node.key);
                node = node.right;
            }
        }
    }
    /**
     * 后序遍历
     * @param node
     */
    public void posOrder(TreeNode node){
        if (node != null){
            posOrder(node.left);
            posOrder(node.right);
            System.out.println(node.key);
        }
    }

    /**
     * 后序遍历（非递归）
     * @param node
     */
    public void posOrder1(TreeNode node){
        Stack<TreeNode> stack1 = new Stack<>();
        Stack<Integer> stack2 = new Stack<>();
        while (node != null || !stack1.empty()){
            while ( node != null){
                stack1.push(node);
                stack2.push(0);
                node = node.left;
            }
            while (!stack1.empty() && stack2.peek() == 1){
                stack2.pop();
                System.out.println(stack1.pop().key);
            }
            if (!stack1.empty()){
                stack2.pop();
                stack2.push(1);
                node = stack1.peek();
                node = node.right;
            }
        }
    }

    /**
     * 后序遍历（非递归）
     * 前序遍历  根--左--右
     * 后序遍历  左--右--根
     * 借用前序遍历算法思想 修改成 根--右--左，然后反转得到  左--右--根
     * @param node
     * @return
     */
    public ArrayList<Integer> posOrder2(TreeNode node){
        ArrayList<Integer> list = new ArrayList<>();
        if (node != null){
            Stack<TreeNode> stack = new Stack<>();
            stack.push(node);
            while (!stack.empty()){
                TreeNode node1 = stack.pop();
                list.add(node1.key);
                if (node1.left != null) stack.push(node1.left);
                if (node1.right != null)stack.push(node1.right);
            }
            Collections.reverse(list);
        }
        return list;
    }
    /**
     * 层序遍历（递归）
     * @param node
     */
    public void levelOrder(TreeNode node){
        if (node == null)return;
        //计算深度
        int depth = depth(node);
        for(int i = 0;i< depth;i++){
            //根据第几层得到所处第几层的所有元素
            leveOrder(node,i);
        }
    }

    /**
     * 层序遍历（非递归）
     * @param node
     */
    public void levelOrder1(TreeNode node){

        if (node == null) return;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(node);
        TreeNode node1;
        while (!queue.isEmpty()){
            node1 = queue.poll();
            System.out.println(node1.key);
            if (node1.left != null)queue.offer(node1.left);
            if (node1.right != null)queue.offer(node1.right);
        }
    }
    /**
     * 得到第几层的所有元素
     * @param node
     * @param level
     */
    public void leveOrder(TreeNode node,int level){

        if (node == null || level < 1)return;
        if ( level == 1){
            System.out.println(node.key);
            return;
        }
        leveOrder(node.left,level - 1);
        leveOrder(node.right,level - 1);
    }
    /**
     * 二叉树的深度
     * @param node
     * @return
     */
    public int depth(TreeNode node){
        if (node == null) return 0;
        int l = depth(node.left);
        int r = depth(node.right);
        if (l > r) return l + 1;
        else return r + 1;
    }

    /**
     * 通过层序遍历构建二叉树
     * @param str 层序遍历的任意参数
     * @return 二叉树
     */
    TreeNode buildTreeByLevelOrder(String str){
        String[] nodeArr = str.split(",");
        List<TreeNode> nodelist = new LinkedList<>();
        for (String s : nodeArr) {
            nodelist.add(new TreeNode(Integer.valueOf(s),null,null,null));
        }
        System.out.println(nodelist.size());
        for(int index=0;index < nodelist.size()/2-1;index++){
            //编号为n的节点他的左子节点编号为2*n 右子节点编号为2*n+1 但是因为list从0开始编号，所以还要+1
            //这里父节点有1（2,3）,2（4,5）,3（6,7）,4（8,9） 但是最后一个父节点有可能没有右子节点 需要单独处理
            nodelist.get(index).left = (nodelist.get(index*2+1));
            nodelist.get(index).right = (nodelist.get(index*2+2));
        }
        //单独处理最后一个父节点 因为它有可能没有右子节点
        int index = nodelist.size()/2-1;
        nodelist.get(index).left = (nodelist.get(index*2+1)); //先设置左子节点
        if(nodelist.size() % 2 == 1){ //如果有奇数个节点，最后一个父节点才有右子节点
            nodelist.get(index).right = (nodelist.get(index*2+2));
        }
        return nodelist.get(0);
    }

    /**
     * 根据前序遍历还原二叉树
     * (在前序序列化的时候，如果遇到为null的节点，则在字符串后面添加“#，”
     当反序列化时，会进行判断，当前的位置是否为"#"，如果为#则不会创建子节点)
     */
    int index = -1;
    TreeNode Deserialize(String str) {
        if(str.length() == 0)
            return null;
        index++;
        int len = str.length();
        if(index >= len){
            return null;
        }
        String[] strr = str.split(",");
        TreeNode node = null;
        if(!strr[index].equals("#")){
            node = new TreeNode(Integer.valueOf(strr[index]),null,null,null);
            node.left = Deserialize(str);
            node.right = Deserialize(str);
        }

        return node;
    }

    /**
     * 反转二叉树
     * 或者
     * 建立一颗二叉树的镜像（如果一个二叉树同此二叉树的镜像是同样的，定义其为对称的。）
     * @param root
     */
    public void Mirror(TreeNode root) {
        if(root == null) {
            return;
        }
        if((root.left == null) && (root.right == null)) {
            return;
        }
        TreeNode temp = root.left;
        root.left = root.right;
        root.right = temp;
        Mirror(root.left);
        Mirror(root.right);
    }

    /**
     * 克隆一颗二叉树
     * @param root
     * @return
     */
    public  TreeNode cloneTree(TreeNode root){
        TreeNode node=null;
        if(root==null) return null;
        node = new TreeNode(root.key,null,null,null);
        node.left = cloneTree(root.left);
        node.right = cloneTree(root.right);
        return node;
    }

    /**
     * 判断两颗二叉树是否相同
     * @param root1
     * @param root2
     * @return
     */
    public boolean sameTree2(TreeNode root1, TreeNode root2){
        //树的结构不一样
        if((root1 == null && root2 != null) || (root1 != null && root2 == null))
            return false;
        //两棵树最终递归到终点时
        if(root1 == null && root2 == null)
            return true;
        if(Integer.valueOf(root1.key).compareTo(Integer.valueOf(root2.key)) != 0)
            return false;
        else
            return sameTree2(root1.left, root2.left) && sameTree2(root1.right, root2.right);
    }
    boolean isValidBST(TreeNode root) {
        return isValidBST(root, null, null);
    }
    boolean isValidBST(TreeNode root, TreeNode min, TreeNode max) {
        if (root == null) return true;
        if (min != null && root.key <= min.key) return false;
        if (max != null && root.key >= max.key) return false;
        return isValidBST(root.left, min, root)
                && isValidBST(root.right, root, max);
    }

    boolean isInBST(TreeNode root, int target) {
        if (root == null) return false;
        if (root.key == target)
            return true;
        if (root.key < target)
            return isInBST(root.right, target);
        if (root.key > target)
            return isInBST(root.left, target);
        return  false;
    }
    boolean isSameTree(TreeNode root1, TreeNode root2) {
        // 都为空的话，显然相同
        if (root1 == null && root2 == null) return true;
        // 一个为空，一个非空，显然不同
        if (root1 == null || root2 == null) return false;
        // 两个都非空，但 val 不一样也不行
        if (root1.key != root2.key) return false;

        // root1 和 root2 该比的都比完了
        return isSameTree(root1.left, root2.left)
                && isSameTree(root1.right, root2.right);
    }

    void traverse(TreeNode root) {
        // root 需要做什么？在这做。
        // 其他的不用 root 操心，抛给框架
        traverse(root.left);
        traverse(root.right);
    }
    void plusOne(TreeNode root) {
        if (root == null) return;
        root.key += 1;

        plusOne(root.left);
        plusOne(root.right);
    }

    TreeNode insertIntoBST(TreeNode root, int val) {
        // 找到空位置插入新节点
        if (root == null) return new TreeNode(val,null,null,null);
        // if (root.val == val)
        //     BST 中一般不会插入已存在元素
        if (root.key < val)
            root.right = insertIntoBST(root.right, val);
        if (root.key > val)
            root.left = insertIntoBST(root.left, val);
        return root;
    }
    TreeNode deleteNode(TreeNode root, int key) {
        if (root == null) return null;
        if (root.key == key) {
            // 这两个 if 把情况 1 和 2 都正确处理了
            if (root.left == null) return root.right;
            if (root.right == null) return root.left;
            // 处理情况 3
            TreeNode minNode = getMin(root.right);
            root.key = minNode.key;
            root.right = deleteNode(root.right, minNode.key);
        } else if (root.key > key) {
            root.left = deleteNode(root.left, key);
        } else if (root.key < key) {
            root.right = deleteNode(root.right, key);
        }
        return root;
    }

    TreeNode getMin(TreeNode node) {
        // BST 最左边的就是最小的
        while (node.left != null) node = node.left;
        return node;
    }
    public static void main(String[] args) {
        try {
            BinarySearchTree bst = new BinarySearchTree();
            System.out.println("查找树是否为空？ " + (bst.isEmpty() ? "是" : "否"));
            int[] keys = new int[] { 15, 6, 18, 3, 7, 13, 20, 2, 9, 4 };
            for (int key : keys) {
                bst.insert(key);
            }
            System.out.println("查找树是否为空？ " + (bst.isEmpty() ? "是" : "否"));
            TreeNode minkeyNode = bst.minElemNode(bst.getRoot());
            System.out.println("最小关键字： " + minkeyNode.getKey());
            testNode(bst, minkeyNode);
            TreeNode maxKeyNode = bst.maxElemNode(bst.getRoot());
            System.out.println("最大关键字： " + maxKeyNode.getKey());
            testNode(bst, maxKeyNode);
            System.out.println("根结点关键字： " + bst.getRoot().getKey());
            testNode(bst, bst.getRoot());
            testTraverse(bst);
            System.out.println("****************************** ");
            testTraverse(bst);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
