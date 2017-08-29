package tools;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Formatter;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
 
public class GetInfo {
     
	public static void main(String[] args) {
               Config();
               getConfig();
		GetInfo.all();
	}
	/*
	  windows��Linux��mac�½Y����һ��
	 */
	 public static void getIpconfig(){
	        Map< String,String> map = System.getenv();
	        System.out.println(map);
 
	        /*windows*/
	        System.out.println(map.get("USERNAME"));//�@ȡ�Ñ���
	        System.out.println(map.get("COMPUTERNAME"));//�@ȡӋ��C��
	        System.out.println(map.get("USERDOMAIN"));//�@ȡӋ��C����
 
	        /*mac*/
	        System.out.println(map.get("USER"));
	    }
	 //������һЩ�|��,�����õ��ĕr���
	    public static void all(){
	        Properties �O���n =System.getProperties();
	        System.out.println("Java���\�Эh���汾��"+�O���n.getProperty("java.version"));
	        System.out.println("Java���\�Эh�������̣�"+�O���n.getProperty("java.vendor"));
	        System.out.println("Java�����̵�URL��"+�O���n.getProperty("java.vendor.url"));
	        System.out.println("Java�İ��b·����"+�O���n.getProperty("java.home"));
	        System.out.println("Java��̓�M�CҎ���汾��"+�O���n.getProperty("java.vm.specification.version"));
	        System.out.println("Java��̓�M�CҎ�������̣�"+�O���n.getProperty("java.vm.specification.vendor"));
	        System.out.println("Java��̓�M�CҎ�����Q��"+�O���n.getProperty("java.vm.specification.name"));
	        System.out.println("Java��̓�M�C���F�汾��"+�O���n.getProperty("java.vm.version"));
	        System.out.println("Java��̓�M�C���F�����̣�"+�O���n.getProperty("java.vm.vendor"));
	        System.out.println("Java��̓�M�C���F���Q��"+�O���n.getProperty("java.vm.name"));
	        System.out.println("Java�\�Еr�h��Ҏ���汾��"+�O���n.getProperty("java.specification.version"));
	 //       System.out.println("Java�\�Еr�h��Ҏ�������̣�"+�O���n.getProperty("java.specification.vender"));
	        System.out.println("Java�\�Еr�h��Ҏ�����Q��"+�O���n.getProperty("java.specification.name"));
	        System.out.println("Java���ʽ�汾̖��"+�O���n.getProperty("java.class.version"));
	        System.out.println("Java���·����"+�O���n.getProperty("java.class.path"));
	        System.out.println("���d��r������·���б�"+�O���n.getProperty("java.library.path"));
	        System.out.println("Ĭ�J���R�r�ļ�·����"+�O���n.getProperty("java.io.tmpdir"));
	        System.out.println("һ��������UչĿ䛵�·����"+�O���n.getProperty("java.ext.dirs"));
	   //     System.out.println("����ϵ�y�����Q��"+�O���n.getProperty("os.name"));
	        System.out.println("����ϵ�y�Ę��ܣ�"+�O���n.getProperty("os.arch"));
	        System.out.println("����ϵ�y�İ汾��"+�O���n.getProperty("os.version"));
	        System.out.println("�ļ��ָ�����"+�O���n.getProperty("file.separator"));
	        //�� unix ϵ�y���ǣ����� 
	        System.out.println("·���ָ�����"+�O���n.getProperty("path.separator"));
	        //�� unix ϵ�y���ǣ�:�� 
	        System.out.println("�зָ�����"+�O���n.getProperty("line.separator"));
	       //�� unix ϵ�y���ǣ�/n��
	        System.out.println("�Ñ����~�����Q��"+�O���n.getProperty("user.name"));
	        System.out.println("�Ñ�����Ŀ䛣�"+�O���n.getProperty("user.home"));
	        System.out.println("�Ñ��Į�ǰ����Ŀ䛣�"+�O���n.getProperty("user.dir"));
	    }
 
    public static void Config(){
        try{
            InetAddress addr = InetAddress.getLocalHost(); 
            String ip=addr.getHostAddress().toString(); //�@ȡ���Cip
            String hostName=addr.getHostName().toString(); //�@ȡ���CӋ��C���Q
            System.out.println("���CIP��"+ip+"\n���C���Q:"+hostName);
            Properties �O���n=System.getProperties();
            System.out.println("����ϵ�y�����Q��"+�O���n.getProperty("os.name"));
            System.out.println("����ϵ�y�İ汾��"+�O���n.getProperty("os.version")); 
        }catch(Exception e){
            e.printStackTrace();
        }
    }
 
 
 
    /**
     * �õ�Ӌ��C��ip��ַ��mac��ַ<br />
     * IP��127.0.0.1<br />
	 * MAC��FE-80-00-00-00-00-00-00-00-00-00-00-00-00-00-01
     */
    public static void getConfig(){
        try{
            InetAddress address = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(address);
            //ni.getInetAddresses().nextElement().getAddress();
            byte[] mac = ni.getHardwareAddress();
            if(mac==null){
            	mac = ni.getInetAddresses().nextElement().getAddress();
            }
 
            String sIP = address.getHostAddress();
            String sMAC = "";
            Formatter formatter = new Formatter();
            for (int i = 0; i < mac.length; i++) {
                sMAC = formatter.format(Locale.getDefault(), "%02X%s", mac[i],
                        (i < mac.length - 1) ? "-" : "").toString();
            }
            System.out.println("IP��" + sIP);
            System.out.println("MAC��" + sMAC);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}