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
		// 처음에는 1번,7만 가능하다는 조건
		boolean first = false;
		// 1번은 한번만 가능하다
		boolean createDone = false;

		MongoClient mongo = null;
		MongoDatabase mydb = null;
		MongoCollection<Document> mycol = null;

		String dbs;
		String col;

		while (run) {
			// 1~7번까지 입력해주세요 작성해야함
			//
			//
			System.out.println("<< 학생 정보 관리 프로그램 >> ");
			System.out.println("1) 데이터베이스 및 컬렉션 입력/선택");
			System.out.println("2) 학생 정보 삽입");
			System.out.println("3) 학생 정보 삭제");
			System.out.println("4) 학생 정보 수정");
			System.out.println("5) 학생 정보 검색 (전체)");
			System.out.println("6) 학생 정보 검색 (조건)");
			System.out.println("7) 종료");
			System.out.println();

			int num;
			System.out.print("메뉴 선택 : ");
			num = scanner.nextInt();
			if (!first && (2 <= num && num <= 6)) {
				System.out.println("처음에는 1번 or 7번을 선택해주세요.");
				continue;
			}
			if (num == 1 && createDone) {
				System.out.println("다시 1번을 선택하실 수 없습니다.");
				continue;
			}

			first = true;

			if (num == 1) {
				// 데이터 베이스 및 컬렉션 입력/선택

				System.out.print("데이터베이스: ");
				dbs = scanner.next();

				System.out.print("컬렉션: ");
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

			// 학생 정보 삽입
			if (num == 2) {

				String sid, sname, grade, sex, email;
				String clg, clgName;
				Document blg = new Document();
				List<String> hobby = new ArrayList<String>();

				System.out.print("학번 : ");
				sid = scanner.next();

				System.out.print("이름 : ");
				sname = scanner.next();

				System.out.print("단과대학  : ");
				clg = scanner.next();
				blg.put("단과대학", clg);
				System.out.print("학과명: ");
				clgName = scanner.next();
				blg.put("학과명", clgName);

				System.out.print("학년 : ");
				grade = scanner.next();

				System.out.print("성별 : ");
				sex = scanner.next();

				System.out.print("이메일 : ");
				email = scanner.next();

				// for hobby
				while (true) {
					String temp;
					System.out.print("취미 (취미를 다 적었다면 end 입력 후 enter) : ");
					temp = scanner.next();
					if (temp.equals("end"))
						break;

					hobby.add(temp);
				}

				Document mydoc = new Document("학번", sid)
						.append("이름", sname)
						.append("소속", blg)
						.append("학년", grade)
						.append("성별", sex)
						.append("이메일", email)
						.append("취미", hobby);

				mycol.insertOne(mydoc);
				System.out.println("문서가 성공적으로 삽입 되었습니다.");
				
			} 
			
			//학생 정보 삭제
			else if (num == 3) {
				String rid;
				System.out.print("학번을 입력하세요 : ");
				rid = scanner.next();

				Document mydoc = mycol.find(eq("학번", rid)).first();

				if (mydoc == null) {
					System.out.println("해당 학번은 존재하지 않습니다.");
					System.out.println("메뉴로 돌아갑니다.");
				} else {
					mycol.deleteOne(eq("학번", rid));
				}
				
				System.out.println("삭제 완료 되었습니다.");
			}
			
			//학생 정보 수정
			else if (num == 4) {
				String uid;
				System.out.print("학번을 입력하세요 : ");
				uid = scanner.next();

				Document mydoc = mycol.find(eq("학번", uid)).first();

				if (mydoc == null) {
					System.out.println("해당 학번은 존재하지 않습니다.");
					System.out.println("메뉴로 돌아갑니다.");
				} else {
					int updateNum;
					System.out.println("1. 학년");
					System.out.println("2. 이메일");
					System.out.println("3. 취미");
					System.out.println("4. 메뉴로 돌아가기");
					System.out.println();
					System.out.print("수정할 필드를 골라주세요: ");
					updateNum = scanner.nextInt();

					System.out.println();

					if (updateNum == 1) {
						String newGrade;
						System.out.print("변경될 학년:");
						newGrade = scanner.next();
						mycol.updateOne(eq("학번", uid), new Document("$set", new Document("학년", newGrade)));

						System.out.println("학년이 수정되었습니다.");
					} else if (updateNum == 2) {
						String newEmail;
						System.out.print("변경될 이메일:");
						newEmail = scanner.next();
						mycol.updateOne(eq("학번", uid), new Document("$set", new Document("이메일", newEmail)));

						System.out.println("이메일이 수정되었습니다.");
					} else if (updateNum == 3) {
						List<String> newHobby = new ArrayList<String>();
						while (true) {
							String temp;
							System.out.print("변경될 취미 (취미를 다 적었다면 end 입력 후 enter) : ");
							temp = scanner.next();
							if (temp.equals("end"))
								break;

							newHobby.add(temp);
						}
						mycol.updateOne(eq("학번", uid), new Document("$set", new Document("취미", newHobby)));

						System.out.println("취미가 수정되었습니다.");
					} else {
						System.out.println("수정하지 않고 메뉴로 돌아갑니다.");
					}
				}
			} 
			
			//학생 정보 검색 (전체)
			else if (num == 5) {
				FindIterable<Document> findDoc = mycol.find().projection(excludeId());

				for (Document doc : findDoc) {
					System.out.println(doc.toJson());
				}
				long rows = mycol.countDocuments();

				System.out.println("학생 수 : " + rows);
			}
			
			//학생 정보 검색 (조건)
			else if (num == 6) {
				String name, grade, clgName;
				List<String> hobby = new ArrayList<String>();

				List<Integer> cand = new ArrayList<Integer>();
				System.out.println("1. 이름");
				System.out.println("2. 학과명");
				System.out.println("3. 학년");
				System.out.println("4. 취미");
				System.out.println("5. 종료");
				System.out.println();
				
				while(true) {
					int temp;
					System.out.print("검색 조합을 차례대로 숫자로 입력해주세요 ( 예 : 1 2 5 ) : ");
					temp = scanner.nextInt();
					if(temp == 5) break;
					cand.add(temp);
				}
				
				Integer[] mycand = cand.toArray(new Integer[cand.size()]);
				FindIterable<Document> findDoc = null;
				
				// 하나만 골랐을때
				if (mycand.length == 1) {
					if(mycand[0] == 1) {
						System.out.print("이름 : ");
						name = scanner.next();
						findDoc = mycol.find(eq("이름", name)).projection(excludeId());
					}
					else if(mycand[0] == 2) {
						System.out.print("학과명: ");
						clgName = scanner.next();
						findDoc = mycol.find(eq("소속.학과명", clgName)).projection(excludeId()); 
					}
					else if(mycand[0] == 3) {
						System.out.print("학년: ");
						grade = scanner.next();
						findDoc = mycol.find(eq("학년", grade)).projection(excludeId());
					}
					else if(mycand[0] == 4) {
						// for hobby
						while (true) {
							String temp;
							System.out.print("취미 (취미를 다 적었다면 end 입력 후 enter) : ");
							temp = scanner.next();
							if (temp.equals("end"))
								break;

							hobby.add(temp);
						}

						findDoc = mycol.find(all("취미", hobby)).projection(excludeId());
					}
				}
				
				// 두개 골랐을때
				else if (cand.size() == 2) {
					
					if(mycand[0] == 1 && mycand[1] == 2) {
						
						System.out.print("이름 : ");
						name = scanner.next();
						
						System.out.print("학과명: ");
						clgName = scanner.next();
						
						findDoc = mycol.find(and(eq("이름", name),eq("소속.학과명", clgName))).projection(excludeId());
					}
					else if(mycand[0] == 1 && mycand[1] == 3) {
						
						System.out.print("이름 : ");
						name = scanner.next();
						
						System.out.print("학년: ");
						grade = scanner.next();
						
						findDoc = mycol.find(and(eq("이름", name),eq("학년", grade))).projection(excludeId());
						
					}
					else if(mycand[0] == 1 && mycand[1] == 4) {
						System.out.print("이름 : ");
						name = scanner.next();
						
						while (true) {
							String temp;
							System.out.print("취미 (취미를 다 적었다면 end 입력 후 enter) : ");
							temp = scanner.next();
							if (temp.equals("end"))
								break;

							hobby.add(temp);
						}
						
						findDoc = mycol.find(and(eq("이름", name),all("취미", hobby))).projection(excludeId());
						
					}
					else if(mycand[0] == 2 && mycand[1] == 3) {
						System.out.print("학과명: ");
						clgName = scanner.next();
						
						System.out.print("학년: ");
						grade = scanner.next();
						
						findDoc = mycol.find(and(eq("소속.학과명", clgName),eq("학년", grade))).projection(excludeId());
						
					}
					else if(mycand[0] == 2 && mycand[1] == 4) {
						System.out.print("학과명: ");
						clgName = scanner.next();
						
						while (true) {
							String temp;
							System.out.print("취미 (취미를 다 적었다면 end 입력 후 enter) : ");
							temp = scanner.next();
							if (temp.equals("end"))
								break;

							hobby.add(temp);
						}
						
						findDoc = mycol.find(and(eq("소속.학과명", clgName),all("취미", hobby))).projection(excludeId());
						
					}
					else if(mycand[0] == 3 && mycand[1] == 4) {
						
						System.out.print("학년: ");
						grade = scanner.next();
						
						while (true) {
							String temp;
							System.out.print("취미 (취미를 다 적었다면 end 입력 후 enter) : ");
							temp = scanner.next();
							if (temp.equals("end"))
								break;

							hobby.add(temp);
						}
						
						findDoc = mycol.find(and(eq("학년", grade),all("취미", hobby))).projection(excludeId());
						
					}
				} 
				
				// 세개 골랐을때
				else if (cand.size() == 3) {
					if(mycand[0] == 1 && mycand[1] == 2 && mycand[2] == 3) {
						
						System.out.print("이름 : ");
						name = scanner.next();

						System.out.print("학과명: ");
						clgName = scanner.next();

						System.out.print("학년 : ");
						grade = scanner.next();

						findDoc = mycol
								.find(and(eq("이름", name),
										eq("소속.학과명", clgName),
										eq("학년", grade))).projection(excludeId());

					}
					if(mycand[0] == 1 && mycand[1] == 2 && mycand[2] == 4) {
						
						System.out.print("이름 : ");
						name = scanner.next();

						System.out.print("학과명: ");
						clgName = scanner.next();

						// for hobby
						while (true) {
							String temp;
							System.out.print("취미 (취미를 다 적었다면 end 입력 후 enter) : ");
							temp = scanner.next();
							if (temp.equals("end"))
								break;

							hobby.add(temp);
						}

						findDoc = mycol
								.find(and(eq("이름", name),
										eq("소속.학과명", clgName),
										all("취미", hobby))).projection(excludeId());

					}
					if(mycand[0] == 2 && mycand[1] == 3 && mycand[2] == 4) {
						
						System.out.print("학과명: ");
						clgName = scanner.next();

						System.out.print("학년 : ");
						grade = scanner.next();

						// for hobby
						while (true) {
							String temp;
							System.out.print("취미 (취미를 다 적었다면 end 입력 후 enter) : ");
							temp = scanner.next();
							if (temp.equals("end"))
								break;

							hobby.add(temp);
						}

						findDoc = mycol
								.find(and(eq("소속.학과명", clgName),
										eq("학년", grade),
										all("취미", hobby))).projection(excludeId());

					}
				}
				
				// 네개 골랐을때
				else if (cand.size() == 4) {
					System.out.print("이름 : ");
					name = scanner.next();

					System.out.print("학과명: ");
					clgName = scanner.next();

					System.out.print("학년 : ");
					grade = scanner.next();

					// for hobby
					while (true) {
						String temp;
						System.out.print("취미 (취미를 다 적었다면 end 입력 후 enter) : ");
						temp = scanner.next();
						if (temp.equals("end"))
							break;

						hobby.add(temp);
					}

					findDoc = mycol
							.find(and(eq("이름", name),
									eq("소속.학과명", clgName),
									eq("학년", grade),
									all("취미", hobby))).projection(excludeId());

				}
				
				for (Document doc : findDoc) {
					System.out.println((doc.toJson()));
				} 
			
			}
			// 종료
			else if (num == 7) {
				run = false;
			}
		}
	}

}