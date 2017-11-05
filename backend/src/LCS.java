public class LCS {
    static boolean[] getSameOfB(String a, String b) {
        boolean[] rst = new boolean[b.length()];
        a = " "+a;
        b = " "+b;
        int[][] dp = new int[a.length()][b.length()];
        for(int i=1; i<a.length(); i++) {
            for(int j=1; j<b.length(); j++) {
                if(a.charAt(i)==b.charAt(j)) {
                    dp[i][j] = dp[i-1][j-1]+1;
                }
                else {
                    dp[i][j] = Math.max(dp[i][j-1], dp[i-1][j]);
                }
            }
        }
        for(int i=a.length()-1, j=b.length()-1; i>0 && j>0; ) {
            if(a.charAt(i)==b.charAt(j)) {
                rst[j-1]=true;
                i--;
                j--;
            }
            else if(dp[i][j-1]>dp[i-1][j]) {
                j--;
            }
            else {
                i--;
            }
        }
        return rst;
    }
}
