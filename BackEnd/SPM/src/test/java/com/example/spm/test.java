import java.util.ArrayList;
import java.util.List;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int val) {
        this.val = val;
    }
}

class GoodLeafPairs {
    private int count = 0;

    public int countPairs(TreeNode root, int distance) {
        traverse(root, distance);
        return count;
    }

    private List<Integer> traverse(TreeNode node, int distance) {
        if (node == null) {
            return new ArrayList<>();
        }
        if (node.left == null && node.right == null) {
            List<Integer> single = new ArrayList<>();
            single.add(0);
            System.out.println("叶子节点 " + node.val + "，路径长度列表：" + single);
            return single;
        }

        List<Integer> leftList = traverse(node.left, distance);
        System.out.println("节点 " + node.val + " 的左子树路径长度列表：" + leftList);

        List<Integer> rightList = traverse(node.right, distance);
        System.out.println("节点 " + node.val + " 的右子树路径长度列表：" + rightList);

        for (int left : leftList) {
            for (int right : rightList) {
                int totalDistance = left + right + 1;
                if (totalDistance <= distance) {
                    count++;
                    System.out.println("找到好叶子节点对，左子树路径长度：" + left + "，右子树路径长度：" + right + "，总路径长度：" + totalDistance);
                }
            }
        }

        List<Integer> result = new ArrayList<>();
        for (int left : leftList) {
            result.add(left + 1);
        }
        for (int right : rightList) {
            result.add(right + 1);
        }

        System.out.println("节点 " + node.val + " 汇总的路径长度列表：" + result);

        return result;
    }

    public static void main(String[] args) {
        GoodLeafPairs solution = new GoodLeafPairs();
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(7);
        int distance = 3;
        System.out.println("好叶子节点对的数量：" + solution.countPairs(root, distance));
    }
}