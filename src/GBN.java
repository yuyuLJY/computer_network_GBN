import java.util.Scanner;
import java.util.Timer;

public class GBN {
	public final int SERVER_PORT = 12340; //�˿ں�
	public final String SERVER_IP = "0.0.0.0"; //IP ��ַ 
	public final int BUFFER_LENGTH = 1026;  //��������С������̫���� UDP ������ ֡�а�����ӦС�� 1480 �ֽڣ� 
	public final int  SEND_WIND_SIZE = 4;//���ʹ��ڴ�СΪ 10,��������ڴ�С��Ϊ 1����Ϊͣ-��Э�� 
	public final static int SEQ_SIZE = 20;//���кŵĸ������� 0~19 ���� 20 �� 
	static boolean[] ack = new boolean [SEQ_SIZE];
	static boolean[] sendData = new boolean [SEQ_SIZE];//��ʾ�ͻ��˷��͵���Ϣ�����
	static int curSeq;//��ǰ���ݰ��� seq 
	static int curAck;//��ǰ�ȴ�ȷ�ϵ� ack 
	int totalSeq;//�յ��İ������� 
	int totalPacket;//��Ҫ���͵İ����� 
	double dataSend_lose_rate = 0; //���ݷ��Ͷ�ʧ��
	double ack_lose_rate = 0.5; //���ݻ�Ӧ��ʧ��
	public static void main(String[] args) {
		//�˵�ѡ��
		/*
		//TODO �˵���
		
		System.out.printf("��ѡ��");
		System.out.printf("-time------Please input 1");
		System.out.printf("-quit------Please input 2");
		System.out.printf("-testgbn------Please input 3");
		GBN g = new GBN();
		Scanner sc = new Scanner(System.in); 
		int op = sc.nextInt();
		switch(op) {
		case 1:
			g.getCurTime ();//
		case 2:
		case 3:
			System.out.printf("please input dataSend_lose_rate & ack_lose_rate ([0,1])");
			int dataSend_lose_rate = sc.nextInt();
			int ack_lose_rate = sc.nextInt();
			//
		}*/
		//ֱ������
		for(int i = 0;i < SEQ_SIZE;i++) {
			sendData[i] = false;//e.g��sentData[1]��ʾsever�Ѿ����ͳ�ȥ�˵�1������
 		}
		
		curAck = 0;//��1��ʼ
		Sever sever = new Sever(0,"no data");
		Client client = new Client(true);
		sever.start();
		//TODO �ܿ��������������������
		client.run(sever);//�������̶߳�����
		
	}
	
	//��ȡ��ǰ������
	public int getcurSeq() {
		return curSeq;
	}
	
	//��ȡ�������ݵĶ�ʧ��
	public double getDataLoss() {
		return dataSend_lose_rate;
	}
	
	//��ȡ���ݻ�Ӧ��ʧ��
	public double getAckLoss() {
		return ack_lose_rate;
	}
	
	
	public void setcurSeq(int curSeq) {
		this.curSeq = curSeq;
	}
	
	
	/**
	 * ��ȡ��ǰʱ��
	 */
	void  getCurTime () {
		
	}

	
	/**
	 * ��ʱ�ش������������������ڵ�����֡��Ҫ�ش� 
	 */
	void timeoutHandler() {
		
	}
}
