import java.util.Timer;

public class GBN {
	public final int SERVER_PORT = 12340; //端口号
	public final String SERVER_IP = "0.0.0.0"; //IP 地址 
	public final int BUFFER_LENGTH = 1026;  //缓冲区大小，（以太网中 UDP 的数据 帧中包长度应小于 1480 字节） 
	public final int  SEND_WIND_SIZE = 4;//发送窗口大小为 10,如果将窗口大小设为 1，则为停-等协议 
	public final static int SEQ_SIZE = 20;//序列号的个数，从 0~19 共计 20 个 
	static boolean[] ack = new boolean [SEQ_SIZE];
	static boolean[] sendData = new boolean [SEQ_SIZE];//表示客户端发送的信息的情况
	int curSeq;//当前数据包的 seq 
	int curAck;//当前等待确认的 ack 
	int totalSeq;//收到的包的总数 
	int totalPacket;//需要发送的包总数 
	//private static Timer timer;
	public static void main(String[] args) {
		//菜单选项
		/*
		//TODO 菜单栏
		String op;
		switch(op) {
		
		}*/
		for(int i = 0;i < SEQ_SIZE;i++) {
			sendData[i] = false;//e.g：sentData[1]表示sever已经发送出去了第1段数据
 		}
		
		Sever sever = new Sever(0,"no data");
		Client client = new Client();
		sever.start();
		//TODO 很可能这个不是在这里设置
		client.run(sever);//把两个线程都创建
		
	}
	
	//获取当前的序列
	public int getcurSeq() {
		return curSeq;
	}
	
	
	public void setcurSeq(int curSeq) {
		this.curSeq = curSeq;
	}
	
	
	/**
	 * 获取当前时间
	 */
	void  getCurTime () {
		
	}

	
	/**
	 * 超时重传处理函数，滑动窗口内的数据帧都要重传 
	 */
	void timeoutHandler() {
		
	}
}
