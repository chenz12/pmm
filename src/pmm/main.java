package pmm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Array;
import java.util.ArrayList;
import ilog.cplex.*;
import ilog.concert.*;
import ilog.cplex.*;


public class main {
	static ArrayList<result> res  = new ArrayList<result>();
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		CSVFileUtil lin = new CSVFileUtil("line.csv");
		//CSVFileUtil old_in = new CSVFileUtil("old.csv");
		//CSVFileUtil n = new CSVFileUtil("new.csv");
		 
		int i=0 ,j=1 ,k =1;
		line[] l = new line[102];
		int[] remain = new int[1139];
		
		//System.out.println(lin.readLine());
		l[0] = new line(lin.readLine().split(","),1);
		for(i=1;i<103;){
			String[] p;
			String s =lin.readLine();
			k++;
			if(s!=null){
			p =s.split(",");
			}
			else{break;}
			if(!p[0].equals(l[i-1].id)){
				//System.out.println("p[0]:"+p[0]+"l[i-1]"+l[i-1].id);
			l[i] = new line(p,k);
			i++;
			j=1;
			}
			else {
				l[i-1].addstop(p, j,k);
				j++;
				}
			
		}
		//print(l);
		//System.out.println(l[123].id);
		//System.out.println(l[1].id);
		
		CSVFileUtil oldin = new CSVFileUtil("old.csv");
		old_demand[] old = new old_demand[2702];
		for(int k1=0;k1<2702;k1++){
			old[k1] = new old_demand(oldin.readLine());
		}
		//printold(old);
		
		
		CSVFileUtil nein = new CSVFileUtil("new.csv");
		new_demand [] ne = new new_demand[236];
		for(int k2=0;k2<236;k2++){
			ne[k2] = new new_demand(nein.readLine());
		}
		//printline(l);
		process_line(l,old);
		//printline(l);
		//System.out.println("�����");
		//printline(l);
		pair(l,ne);
		printnew(ne);
		value(remain,l);
		print(res);
		int count=0;
		for(int e:remain){
			if(e<0){
				e = 0;
			}
			System.out.print(count+" ");
			System.out.println(e);
			count++;
		}
		System.out.println("��ʼ");
		int n=0;
		for(line a : l){
			int temp=0;
			for(station s : a.stop){
				if(s!=null){
					temp = s.sequence;
				}
			}
			n += temp;
			System.out.println(n);
		}
		int i2=0;
		System.out.println("����");
		for(result w : res){
			
			for(double e : w.price){
				
				i2++;
			}
			System.out.println(i2);
			
		}
		
		double[] r1 = new double[106];
		r1 = new cplex(remain,res).solve();
		int[] r2 = cplex.inter;
		int i1=0;
		String[] ls = new String[106];
		double[] ps = new double[106];
		String[] pis = new String[106];
		int[] stas= new int[106];
		int[] ends = new int[106];
		for(result w : res){
			
			for(double e : w.price){
				
				ps[i1]=e;
				i1++;
			}
			
			
		}
		i1=0;
		for(result w : res){
			
			for(String e : w.lines){
				ls[i1]=e;
				i1++;
				
			}
		}
		i1=0;
		for(result w : res){
			
			for(String e : w.lines){
				pis[i1]=w.id;
				i1++;
				
			}
		}
		i1=0;
		for(result w : res){
			
			for(int e : w.starts){
				stas[i1]=e;
				i1++;
				
			}
		}
		i1=0;
		for(result w : res){
			
			for(int e : w.ends){
				ends[i1]=e;
				i1++;
				
			}
		}
		System.out.println("��ʼ��");
		proc[] rt = pro(ls,ps,r1,pis,stas, ends);
		double[] nr = f(l,rt);
		for(double ty : nr){
			System.out.println(ty);
		}
		System.out.println("ID:");
		for(proc ty : rt){
			if(ty.id.equals("0e38f8d3-01aa-4c40-a721-5fe929b6ac07")){
				for(new_demand a : ne){
					if(a.passenger_id.equals(ty.pa_id))
					{
						System.out.print(a.passenger_id+"  ");
						System.out.print("  "+a.start_lat);
						System.out.print("  "+a.start_lon);
						System.out.print("  "+a.end_lat);
						System.out.print("  "+a.end_lon);
						System.out.print("  "+ty.starts);
						System.out.println("  "+ty.ends);
						
					}
				}
				
				
			}
		}
		
		
		/*
		
		//System.out.println(e);
		
		//System.out.println("����������"+count);*/
	}
	static class proc{
		String id;
		double price;
		String pa_id;
		int starts;
		int ends;

		proc(String a, double b,String c, int d, int e){
			id =a;
			price = b;
			pa_id = c;
			starts  = d;
			ends = e;

		}
	}
	
	static proc[] pro(String[] a,double[] b, double[] c,String[] d, int[] e, int[] f){
		proc[] res = new proc[106];
		for(int i=0;i<c.length;i++){
			if(c[i]>0){
				res[i] = new proc(a[i],b[i],d[i],e[i],f[i]);
			}
			else{res[i] = new proc("",0,"",0,0);}
		}
		return res;
	}
	
	static double[] f(line[] a, proc[] b){
		double[] r = new double[102];
		int[] q = new int[102];
		int p=0;
		for(proc i : b){
			p=0;
			for(line j : a){
				if(i.id.equals(j.id)){
					r[p] += i.price;
					for(int k=i.starts;k<i.ends;k++){
						j.stop[k].seatcount--;
						
					}
				}
				p++;
			}
		}
		
		
		
		/*for(line i : a){
			r[p]=per(i);
			p++;
		}*/
		return r;
	}
	
	static void reven(int[][] a, new_demand[] b){
		
	}
	
	static void process_line(line[] a, old_demand[] b){
		int start=0, end=0;
		for(int i=0;i<b.length;i++){
			for(int j=0;j<a.length;j++){
				if(b[i].line_id.equals(a[j].id)){
					for(int p=0;a[j].stop[p]!=null;p++){
						station s = a[j].stop[p];
						if(b[i].station_from.equals(s.id)){
							start = s.sequence;
						}
						if(b[i].station_to.equals(s.id)){
							end = s.sequence;
							
						}
					}
					for(int k=start;k<end;k++){
						a[j].stop[k].seatcount--;
					}
					break;
				}
			}
		}
	}
	static void value(int[] a, line[] b){
		int k=0;
		for(int i=0;i<102;i++){
			for(int j=0;b[i].stop[j]!=null;j++){
				a[k] = b[i].stop[j].seatcount;
				k++;
			}
		}
	}
	
	static void pair(line[] a, new_demand[] b){
		for(int i=0;i<236;i++){
			line tem1 = null;
			line tem2 = null;
			for(int j=0;j<102;j++){
				double lat1=0,lat2=0,lon1=0,lon2=0;
				for(int k=0;a[j].stop[k]!=null;k++){
					
					
					station s= a[j].stop[k];
					
					
					if(sdist(s,b[i])<=0.4&&b[i].depar_time<=s.arrivetime&&b[i].depar_time+1800<=s.arrivetime&&is1(b[i],a[j])&&!a[j].equals(tem1)){
						b[i].start.add(s.num);
						b[i].starts.add(s.sequence);
						lat1 = s.lat;
						lon1 = s.lon;
						 tem1 = a[j];
					}
					if(edist(s,b[i])<=0.4&&is2(b[i],a[j])&&!a[j].equals(tem2)&&a[j].equals(tem1)){
						b[i].end.add(s.num);
						b[i].ends.add(s.sequence);
						lat2 = s.lat;
						lon2 = s.lon;
						tem2 = a[j];
						b[i].price.add(line.price(line.dist(lat1, lon1, lat2, lon2)));
						b[i].lin.add(j);
						b[i].lins.add(a[j].id);
						//lat1=lat2=lon1=lon2 =0 ;
					}
					
				}
			}
		}
	}
	

	
	static boolean is1(new_demand a, line b){
		for(int k=0;b.stop[k]!=null;k++){
			station s= b.stop[k];
			if(edist(s,a)<=0.4){
				return true;
			}
		}
		return false;
	}
	
	static boolean is2(new_demand a, line b){
		for(int k=0;b.stop[k]!=null;k++){
			station s= b.stop[k];
			if(sdist(s,a)<=0.4&&a.depar_time<=s.arrivetime&&a.depar_time+1800<=s.arrivetime){
				return true;
			}
		}
		return false;
	}

	static double sdist(station a, new_demand b){
		return line.dist(a.lat, a.lon, b.start_lat, b.start_lon);
	}
	
	static double per(line a){
		double temp = a.stop[0].seatcount;
		for(int i=1;a.stop[i]!=null;i++){
			if(a.stop[i].seatcount<temp){
				temp = a.stop[i].seatcount;
			}
		}
		if(a.stop[0].seatcount==0){
			return 1;
		}
		else {return 1-temp/a.stop[0].seatcount;}
	}
	
	
	static double edist(station a, new_demand b){
		return line.dist(a.lat, a.lon, b.end_lat, b.end_lon);
	}
	
	static void printline(line[] a){
		System.out.println("�����ʣ�");
		for(int i=0;i<102;i++){
			
			/*
			System.out.println(a[i].id+" ");
			//System.out.println(a[i].stop.length);
			for(int j=0;a[i].stop[j]!=null;j++){
			System.out.println("�ڼ��Σ�"+a[i].stop[j].sequence+"ʣ����λ��"+a[i].stop[j].seatcount);
			System.out.println(a[i].stop[j].num);
			}
		
			*/
			if(per(a[i])!=0){
			//System.out.println(per(a[i]));
			}
			
			System.out.println(per(a[i]));
		}
	}
	static void printold(old_demand[] a){
		for(int i=0;i<2702;i++){
			System.out.print(a[i].line_id+" ");
			System.out.println(a[i].station_from);
			/*for(int j=0;j<a[i].stop.length;j++){
				
			}
			System.out.println("");*/
		}
	}
	static void printnew(new_demand[] a){
		for(int i=0;i<236;i++){
			if(!a[i].start.isEmpty()&&!a[i].end.isEmpty()&&!a[i].start.equals(a[i].end)){
				result p = new result(a[i].passenger_id,a[i].start,a[i].end,a[i].price,a[i].lin,a[i].lins,a[i].starts,a[i].ends);
				p.clean();
				res.add(p);
			//System.out.println(a[i].passenger_id);
				/*
			//System.out.println(a[i].depar_time);
			System.out.println("start_stop:");
			for(int s : a[i].start){
				System.out.print(s+"    ");
			}
			System.out.println("");
			System.out.println("end_stop:");
			for(int s : a[i].end){
				System.out.print(s+"    ");
			}
			System.out.println("");
			/*for(int j=0;j<a[i].stop.length;j++){
				
			}
			System.out.println("");*/
		}
		}
	}
	
	
	static void print(ArrayList<result> a){
		int n =0;
		for(result b : a){
			n += b.start.size();
			System.out.println(b.id);
			System.out.println("start:");
			for(int c: b.start){
				System.out.println(c);
			}
			System.out.println("end:");
			for(int d:b.end){
				System.out.println(d);
			}
			System.out.println("price:");
			for(double e:b.price){
				//if(e!=4.0){
				System.out.println(e);
				//}
			}
			
		}
		System.out.println("column:"+n);
		System.out.println("lines:"+a.size());
	}
}

class station{
	String id ="";
	int seatcount;
	int sequence;
	int arrivetime;
	double lat;
	double lon;
	double mile;
	int num;
	station(String a, int b, int c, String d, double e, double f,int i){
		id =a;
		seatcount = b;
		sequence = c;
		arrivetime=line.time(d);
		lat = e;
		lon = f;
		mile = 0;
		num = i;
	}
}
class line{
	String id = "";
	
	station[] stop = new station[21];
	line(String[] p,int i){	
			
			this.id = p[0];
			stop[0] = new station(p[1], Integer.parseInt(p[2]),Integer.parseInt(p[3]),p[4],Double.parseDouble(p[5]),Double.parseDouble(p[6]),i);
	}
	static int time(String s){
		String[] res = s.split(":");
		int r = Integer.parseInt(res[0])*3600+60*Integer.parseInt(res[1])+Integer.parseInt(res[2]);
		return r;
		
	}
	void addstop(String[] p, int i, int j){
		stop[i] = new station(p[1], Integer.parseInt(p[2]),Integer.parseInt(p[3]),p[4],Double.parseDouble(p[5]),Double.parseDouble(p[6]),j);
	}
	static double dist(double a, double b, double x, double y){
		return (Math.abs(a-x)*111195+Math.abs(b-y)*85180)/1000;
	}
	
	static double price(double a){
		if(a<5){
			return 4;
		}
		else if(a<10){
			return 3.5+0.5*(a-5);
		}
		else if(a<15){
			return 0.4*(a-10)+6;
		}
		else if(a<20){
			return 8+(a-15)*0.3;
		}
		else{
			return 9.5+0.2*(a-20);
		}
	}
}

class old_demand{
	String line_id="";
	String station_from="";
	String station_to="";
	old_demand(String s){
		String[] p = s.split(",");
		this.line_id = p[0];
		this.station_from = p[1];
		this.station_to = p[2];
	}
}

class new_demand{
	String passenger_id;
	double start_lat;
	double start_lon;
	double end_lat;
	double end_lon;
	int depar_time;
	ArrayList<Double> price = new ArrayList<Double> ();
	ArrayList<Integer> start = new ArrayList<Integer> ();
	ArrayList<Integer> end = new ArrayList<Integer> ();
	ArrayList<Integer> lin = new ArrayList<Integer>();
	ArrayList<String> lins = new ArrayList<String>();
	ArrayList<Integer> starts = new ArrayList<Integer> ();
	ArrayList<Integer> ends = new ArrayList<Integer> ();
	new_demand(String s){
		String[] p = s.split(",");
		this.passenger_id = p[0];
		this.start_lat = Double.parseDouble(p[1]);
		this.start_lon = Double.parseDouble(p[2]);
		this.end_lat = Double.parseDouble(p[3]);
		this.end_lon = Double.parseDouble(p[4]);
		this.depar_time = line.time(p[5]);
		
	}
}
class result{
		String id;
		ArrayList<Integer> start;
		ArrayList<Integer> end;
		ArrayList<Double> price;
		ArrayList<Integer> line;
		ArrayList<String> lines;
		ArrayList<Integer> starts;
		ArrayList<Integer> ends;
		result(String a, ArrayList<Integer> b, ArrayList<Integer> c, ArrayList<Double> d, ArrayList<Integer> e,ArrayList<String> f,ArrayList<Integer> g,ArrayList<Integer> h){
			this.id = a;
			start = b;
			end = c;
			price = d;
			line = e;
			lines = f;
			starts = g;
			ends = h;
		}
		void clean(){
			for(int i=start.size()-1;i>end.size()-1;i--){
				start.remove(i);
				starts.remove(i);
			}
		}
}

