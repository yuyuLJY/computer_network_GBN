//�ͻ��˶��߳� ����������
	class Client extends Thread{
		
		public void run(Sever s) {
			System.out.println("Client��������");
			responseAck(s);
			//TODO ������������
		}
		
		public void responseAck(Sever s) {
			//��ͻ��˵�getAck�������ͣ��Ѿ�ȷ�ϵ����ݶ�
			s.getAck(s.Id);
		}
		
	}
