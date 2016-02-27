package pmm;

import java.util.ArrayList;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.*;

public class cplex {
	static int[][] matrix = new int[1139][106];
	static int[] c = new int[1139];
	static double[] p = new double[106];
	static int[] inter = new int[79];
	cplex(int[] a, ArrayList<result> b){
		c = a;
		int q =0;
		for(int i=0;i<b.size();i++){
			
			for(int j=0;j<b.get(i).start.size();j++){
				p[q] = b.get(i).price.get(j);
				q++;
				for(int k=b.get(i).start.get(j)-1;k<b.get(i).end.get(j);k++){
					matrix[k][q-1] = 1;
				}
			}
			inter[i] = q;
		}
	}
	public static void p(){
		for(int i=0;i<79;i++){
		System.out.println(inter[i]);
		}
	}
	public static double[] solve(){
		try{
			IloCplex cl = new IloCplex();
			IloNumVar[] x = new IloNumVar[106];
			x = cl.boolVarArray(106);
			IloLinearNumExpr obj = cl.linearNumExpr();
			for(int i=0;i<106;i++){
				obj.addTerm(p[i], x[i]);
			}
			cl.addMaximize(obj);
			
			ArrayList<con> tem = new ArrayList<con>();
			tem = contain(matrix);

			
			for(con i:tem){
				IloLinearNumExpr exp = cl.linearNumExpr();
				for(int j:i.a){
				if(matrix[i.i][j]==1){
					exp.addTerm(1.0, x[j]);
				}
				cl.addLe(exp,c[i.i]);
				
			}
			
			}
			int j=0;
			for(int i=0;i<79;i++){
				IloLinearNumExpr ex = cl.linearNumExpr();
				for(;j<inter[i];j++){
					ex.addTerm(1.0, x[j]);
				}
				cl.addLe(ex, 1);
			}
			cl.solve();
			for(double i : cl.getValues(x)){
				System.out.println(i);
			}
			return cl.getValues(x);
			
			
			
		}catch (IloException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<con> contain(int[][] a){
		ArrayList<con> res = new ArrayList<con>();
		System.out.println(a.length);
		for(int i=0;i<a.length;i++){
			if(iscontain(a[i])){
				ArrayList<Integer> o = new ArrayList<Integer>();
				
				con t = new con(i,o);
				
				for(int j=0;j<a[i].length;j++){
					if(a[i][j]==1){
						t.a.add(j);
					}
				}
				res.add(t);
			}

			
			
		}
		return res;
	}
	public static boolean iscontain(int[] a){
		for(int i=0;i<a.length;i++){
			if(a[i]==1){
				return true;
			}
		}
		return false;
	}
	
	static class con{
		int i;
		ArrayList<Integer> a;
		con(int a, ArrayList<Integer> b){
			i = a; 
			this.a = b;
		}
	}
}

