import java.util.Timer;

public class GBN {
	public final int SERVER_PORT = 12340; //�˿ں�
	public final String SERVER_IP = "0.0.0.0"; //IP ��ַ 
	public final int BUFFER_LENGTH = 1026;  //��������С������̫���� UDP ������ ֡�а�����ӦС�� 1480 �ֽڣ� 
	public final int  SEND_WIND_SIZE = 4;//���ʹ��ڴ�СΪ 10,��������ڴ�С��Ϊ 1����Ϊͣ-��Э�� 
	public final static int SEQ_SIZE = 20;//���кŵĸ������� 0~19 ���� 20 �� 
	static boolean[] ack = new boolean [SEQ_SIZE];
	static boolean[] sendData = new boolean [SEQ_SIZE];//��ʾ�ͻ��˷��͵���Ϣ�����
	int curSeq;//��ǰ���ݰ��� seq 
	int curAck;//��ǰ�ȴ�ȷ�ϵ� ack 
	int totalSeq;//�յ��İ������� 
	int totalPacket;//��Ҫ���͵İ����� 
	//private static Timer timer;
	public static void main(String[] args) {
		//�˵�ѡ��
		/*
		//TODO �˵���
		String op;
		switch(op) {
		
		}*/
		for(int i = 0;i < SEQ_SIZE;i++) {
			sendData[i] = false;//e.g��sentData[1]��ʾsever�Ѿ����ͳ�ȥ�˵�1������
 		}
		
		Sever sever = new Sever(0,"no data");
		Client client = new Client();
		sever.start();
		//TODO �ܿ��������������������
		client.run(sever);//�������̶߳�����
		
	}
	
	//��ȡ��ǰ������
	public int getcurSeq() {
		return curSeq;
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
