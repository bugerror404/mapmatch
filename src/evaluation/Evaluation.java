package evaluation;

import Tool.Tool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Evaluation {
	public static HashSet<String> getTrueRoute(String routefile, String edgefile) throws IOException, InterruptedException {
		HashSet<String> nodeSet = new HashSet<String>();
		BufferedReader br = new BufferedReader(new FileReader(routefile));
		String sBuf = null;
		while((sBuf = br.readLine()) != null) {
			if(!sBuf.contains("SimpleData name=\"id\""))
				continue;
			Matcher m = Pattern.compile("<SimpleData name=\"id\">(.*?)</SimpleData>").matcher(sBuf);
			while(m.find()) {
				String nid = m.group(1);
				nodeSet.add(nid);
			}
		}
		br.close();
		HashSet<String> route = new HashSet<String>();
		br = new BufferedReader(new FileReader(routefile));
		sBuf = null;
		while((sBuf = br.readLine()) != null) {
			if(!sBuf.contains("SimpleData name=\"id\""))
				continue;
			Matcher m = Pattern.compile("<SimpleData name=\"id\">(.*?)</SimpleData>").matcher(sBuf);
			while(m.find()) {
				String nid = m.group(1);
				String[] cmd = {"sh", "-c", String.format("grep ',%s,' %s", nid, edgefile)};
				Process process = Tool.callShell(cmd);
				BufferedReader inr = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String ins = null;
				while((ins = inr.readLine()) != null) {
					String edge = ins.split(",")[0];
					String nStart = ins.split(",")[1];
					String nEnd = ins.split(",")[2];
					if(nodeSet.contains(nStart) && nodeSet.contains(nEnd))
						route.add(edge);
				}
				process.waitFor();
				inr.close();
			}
		}
		br.close();
		return route;
	}
	
	public static double getAccuracy(String candfile, HashSet<String> route) throws IOException {
		double n = 0;
		double p = 0;
		BufferedReader br = new BufferedReader(new FileReader(candfile));
		String sBuf = null;
		while((sBuf = br.readLine()) != null) {
			if(!sBuf.contains("<tag k=\"name\" v= \"candidate"))
				continue;
			n++;
			String[] ss = sBuf.split(",");
			for(int i = 4; i < ss.length; i += 2) {
				String edge = ss[i];
				if(route.contains(edge)) {
					p++;
					break;
				}
			}
		}
		br.close();
		return p/n;
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
	}

}
