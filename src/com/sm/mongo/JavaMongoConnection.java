package com.sm.mongo;

import static com.mongodb.client.model.Filters.all;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.excludeId;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoCommandException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class JavaMongoConnection {

	public static void main(String args[]) {

		Scanner scanner = new Scanner(System.in);
		boolean run = true;
		// ó������ 1��,7�� �����ϴٴ� ����
		boolean first = false;
		// 1���� �ѹ��� �����ϴ�
		boolean createDone = false;

		MongoClient mongo = null;
		MongoDatabase mydb = null;
		MongoCollection<Document> mycol = null;

		String dbs;
		String col;

		while (run) {
			// 1~7������ �Է����ּ��� �ۼ��ؾ���
			//
			//
			System.out.println("<< �л� ���� ���� ���α׷� >> ");
			System.out.println("1) �����ͺ��̽� �� �÷��� �Է�/����");
			System.out.println("2) �л� ���� ����");
			System.out.println("3) �л� ���� ����");
			System.out.println("4) �л� ���� ����");
			System.out.println("5) �л� ���� �˻� (��ü)");
			System.out.println("6) �л� ���� �˻� (����)");
			System.out.println("7) ����");
			System.out.println();

			int num;
			System.out.print("�޴� ���� : ");
			num = scanner.nextInt();
			if (!first && (2 <= num && num <= 6)) {
				System.out.println("ó������ 1�� or 7���� �������ּ���.");
				continue;
			}
			if (num == 1 && createDone) {
				System.out.println("�ٽ� 1���� �����Ͻ� �� �����ϴ�.");
				continue;
			}

			first = true;

			if (num == 1) {
				// ������ ���̽� �� �÷��� �Է�/����

				System.out.print("�����ͺ��̽�: ");
				dbs = scanner.next();

				System.out.print("�÷���: ");
				col = scanner.next();
				
				
				// Creating a client
				mongo = new MongoClient("localhost", 27017);
				System.out.println("Connected to the database successfully");

				System.out.println(mongo);
				// Accessing the database
				mydb = mongo.getDatabase(dbs);
				System.out.println("Connected to the database successfully");

				System.out.println(mydb);
				
				try {
				mydb.createCollection(col);
				System.out.println("Collection created successfully");
				} catch(MongoCommandException e) {
					System.err.println("Collection Exists");
				}
				mycol = mydb.getCollection(col);
				System.out.println("Collection Col selected succesfully");

				System.out.println(mycol);
				createDone = true;
			}

			// �л� ���� ����
			if (num == 2) {

				String sid, sname, grade, sex, email;
				String clg, clgName;
				Document blg = new Document();
				List<String> hobby = new ArrayList<String>();

				System.out.print("�й� : ");
				sid = scanner.next();

				System.out.print("�̸� : ");
				sname = scanner.next();

				System.out.print("�ܰ�����  : ");
				clg = scanner.next();
				blg.put("�ܰ�����", clg);
				System.out.print("�а���: ");
				clgName = scanner.next();
				blg.put("�а���", clgName);

				System.out.print("�г� : ");
				grade = scanner.next();

				System.out.print("���� : ");
				sex = scanner.next();

				System.out.print("�̸��� : ");
				email = scanner.next();

				// for hobby
				while (true) {
					String temp;
					System.out.print("��� (��̸� �� �����ٸ� end �Է� �� enter) : ");
					temp = scanner.next();
					if (temp.equals("end"))
						break;

					hobby.add(temp);
				}

				Document mydoc = new Document("�й�", sid)
						.append("�̸�", sname)
						.append("�Ҽ�", blg)
						.append("�г�", grade)
						.append("����", sex)
						.append("�̸���", email)
						.append("���", hobby);

				mycol.insertOne(mydoc);
				System.out.println("������ ���������� ���� �Ǿ����ϴ�.");
				
			} 
			
			//�л� ���� ����
			else if (num == 3) {
				String rid;
				System.out.print("�й��� �Է��ϼ��� : ");
				rid = scanner.next();

				Document mydoc = mycol.find(eq("�й�", rid)).first();

				if (mydoc == null) {
					System.out.println("�ش� �й��� �������� �ʽ��ϴ�.");
					System.out.println("�޴��� ���ư��ϴ�.");
				} else {
					mycol.deleteOne(eq("�й�", rid));
				}
				
				System.out.println("���� �Ϸ� �Ǿ����ϴ�.");
			}
			
			//�л� ���� ����
			else if (num == 4) {
				String uid;
				System.out.print("�й��� �Է��ϼ��� : ");
				uid = scanner.next();

				Document mydoc = mycol.find(eq("�й�", uid)).first();

				if (mydoc == null) {
					System.out.println("�ش� �й��� �������� �ʽ��ϴ�.");
					System.out.println("�޴��� ���ư��ϴ�.");
				} else {
					int updateNum;
					System.out.println("1. �г�");
					System.out.println("2. �̸���");
					System.out.println("3. ���");
					System.out.println("4. �޴��� ���ư���");
					System.out.println();
					System.out.print("������ �ʵ带 ����ּ���: ");
					updateNum = scanner.nextInt();

					System.out.println();

					if (updateNum == 1) {
						String newGrade;
						System.out.print("����� �г�:");
						newGrade = scanner.next();
						mycol.updateOne(eq("�й�", uid), new Document("$set", new Document("�г�", newGrade)));

						System.out.println("�г��� �����Ǿ����ϴ�.");
					} else if (updateNum == 2) {
						String newEmail;
						System.out.print("����� �̸���:");
						newEmail = scanner.next();
						mycol.updateOne(eq("�й�", uid), new Document("$set", new Document("�̸���", newEmail)));

						System.out.println("�̸����� �����Ǿ����ϴ�.");
					} else if (updateNum == 3) {
						List<String> newHobby = new ArrayList<String>();
						while (true) {
							String temp;
							System.out.print("����� ��� (��̸� �� �����ٸ� end �Է� �� enter) : ");
							temp = scanner.next();
							if (temp.equals("end"))
								break;

							newHobby.add(temp);
						}
						mycol.updateOne(eq("�й�", uid), new Document("$set", new Document("���", newHobby)));

						System.out.println("��̰� �����Ǿ����ϴ�.");
					} else {
						System.out.println("�������� �ʰ� �޴��� ���ư��ϴ�.");
					}
				}
			} 
			
			//�л� ���� �˻� (��ü)
			else if (num == 5) {
				FindIterable<Document> findDoc = mycol.find().projection(excludeId());

				for (Document doc : findDoc) {
					System.out.println(doc.toJson());
				}
				long rows = mycol.countDocuments();

				System.out.println("�л� �� : " + rows);
			}
			
			//�л� ���� �˻� (����)
			else if (num == 6) {
				String name, grade, clgName;
				List<String> hobby = new ArrayList<String>();

				List<Integer> cand = new ArrayList<Integer>();
				System.out.println("1. �̸�");
				System.out.println("2. �а���");
				System.out.println("3. �г�");
				System.out.println("4. ���");
				System.out.println("5. ����");
				System.out.println();
				
				while(true) {
					int temp;
					System.out.print("�˻� ������ ���ʴ�� ���ڷ� �Է����ּ��� ( �� : 1 2 5 ) : ");
					temp = scanner.nextInt();
					if(temp == 5) break;
					cand.add(temp);
				}
				
				Integer[] mycand = cand.toArray(new Integer[cand.size()]);
				FindIterable<Document> findDoc = null;
				
				// �ϳ��� �������
				if (mycand.length == 1) {
					if(mycand[0] == 1) {
						System.out.print("�̸� : ");
						name = scanner.next();
						findDoc = mycol.find(eq("�̸�", name)).projection(excludeId());
					}
					else if(mycand[0] == 2) {
						System.out.print("�а���: ");
						clgName = scanner.next();
						findDoc = mycol.find(eq("�Ҽ�.�а���", clgName)).projection(excludeId()); 
					}
					else if(mycand[0] == 3) {
						System.out.print("�г�: ");
						grade = scanner.next();
						findDoc = mycol.find(eq("�г�", grade)).projection(excludeId());
					}
					else if(mycand[0] == 4) {
						// for hobby
						while (true) {
							String temp;
							System.out.print("��� (��̸� �� �����ٸ� end �Է� �� enter) : ");
							temp = scanner.next();
							if (temp.equals("end"))
								break;

							hobby.add(temp);
						}

						findDoc = mycol.find(all("���", hobby)).projection(excludeId());
					}
				}
				
				// �ΰ� �������
				else if (cand.size() == 2) {
					
					if(mycand[0] == 1 && mycand[1] == 2) {
						
						System.out.print("�̸� : ");
						name = scanner.next();
						
						System.out.print("�а���: ");
						clgName = scanner.next();
						
						findDoc = mycol.find(and(eq("�̸�", name),eq("�Ҽ�.�а���", clgName))).projection(excludeId());
					}
					else if(mycand[0] == 1 && mycand[1] == 3) {
						
						System.out.print("�̸� : ");
						name = scanner.next();
						
						System.out.print("�г�: ");
						grade = scanner.next();
						
						findDoc = mycol.find(and(eq("�̸�", name),eq("�г�", grade))).projection(excludeId());
						
					}
					else if(mycand[0] == 1 && mycand[1] == 4) {
						System.out.print("�̸� : ");
						name = scanner.next();
						
						while (true) {
							String temp;
							System.out.print("��� (��̸� �� �����ٸ� end �Է� �� enter) : ");
							temp = scanner.next();
							if (temp.equals("end"))
								break;

							hobby.add(temp);
						}
						
						findDoc = mycol.find(and(eq("�̸�", name),all("���", hobby))).projection(excludeId());
						
					}
					else if(mycand[0] == 2 && mycand[1] == 3) {
						System.out.print("�а���: ");
						clgName = scanner.next();
						
						System.out.print("�г�: ");
						grade = scanner.next();
						
						findDoc = mycol.find(and(eq("�Ҽ�.�а���", clgName),eq("�г�", grade))).projection(excludeId());
						
					}
					else if(mycand[0] == 2 && mycand[1] == 4) {
						System.out.print("�а���: ");
						clgName = scanner.next();
						
						while (true) {
							String temp;
							System.out.print("��� (��̸� �� �����ٸ� end �Է� �� enter) : ");
							temp = scanner.next();
							if (temp.equals("end"))
								break;

							hobby.add(temp);
						}
						
						findDoc = mycol.find(and(eq("�Ҽ�.�а���", clgName),all("���", hobby))).projection(excludeId());
						
					}
					else if(mycand[0] == 3 && mycand[1] == 4) {
						
						System.out.print("�г�: ");
						grade = scanner.next();
						
						while (true) {
							String temp;
							System.out.print("��� (��̸� �� �����ٸ� end �Է� �� enter) : ");
							temp = scanner.next();
							if (temp.equals("end"))
								break;

							hobby.add(temp);
						}
						
						findDoc = mycol.find(and(eq("�г�", grade),all("���", hobby))).projection(excludeId());
						
					}
				} 
				
				// ���� �������
				else if (cand.size() == 3) {
					if(mycand[0] == 1 && mycand[1] == 2 && mycand[2] == 3) {
						
						System.out.print("�̸� : ");
						name = scanner.next();

						System.out.print("�а���: ");
						clgName = scanner.next();

						System.out.print("�г� : ");
						grade = scanner.next();

						findDoc = mycol
								.find(and(eq("�̸�", name),
										eq("�Ҽ�.�а���", clgName),
										eq("�г�", grade))).projection(excludeId());

					}
					if(mycand[0] == 1 && mycand[1] == 2 && mycand[2] == 4) {
						
						System.out.print("�̸� : ");
						name = scanner.next();

						System.out.print("�а���: ");
						clgName = scanner.next();

						// for hobby
						while (true) {
							String temp;
							System.out.print("��� (��̸� �� �����ٸ� end �Է� �� enter) : ");
							temp = scanner.next();
							if (temp.equals("end"))
								break;

							hobby.add(temp);
						}

						findDoc = mycol
								.find(and(eq("�̸�", name),
										eq("�Ҽ�.�а���", clgName),
										all("���", hobby))).projection(excludeId());

					}
					if(mycand[0] == 2 && mycand[1] == 3 && mycand[2] == 4) {
						
						System.out.print("�а���: ");
						clgName = scanner.next();

						System.out.print("�г� : ");
						grade = scanner.next();

						// for hobby
						while (true) {
							String temp;
							System.out.print("��� (��̸� �� �����ٸ� end �Է� �� enter) : ");
							temp = scanner.next();
							if (temp.equals("end"))
								break;

							hobby.add(temp);
						}

						findDoc = mycol
								.find(and(eq("�Ҽ�.�а���", clgName),
										eq("�г�", grade),
										all("���", hobby))).projection(excludeId());

					}
				}
				
				// �װ� �������
				else if (cand.size() == 4) {
					System.out.print("�̸� : ");
					name = scanner.next();

					System.out.print("�а���: ");
					clgName = scanner.next();

					System.out.print("�г� : ");
					grade = scanner.next();

					// for hobby
					while (true) {
						String temp;
						System.out.print("��� (��̸� �� �����ٸ� end �Է� �� enter) : ");
						temp = scanner.next();
						if (temp.equals("end"))
							break;

						hobby.add(temp);
					}

					findDoc = mycol
							.find(and(eq("�̸�", name),
									eq("�Ҽ�.�а���", clgName),
									eq("�г�", grade),
									all("���", hobby))).projection(excludeId());

				}
				
				for (Document doc : findDoc) {
					System.out.println((doc.toJson()));
				} 
			
			}
			// ����
			else if (num == 7) {
				run = false;
			}
		}
	}

}