package com.srin23.playfair.service;

import java.awt.AlphaComposite;
import java.util.ArrayList;
import java.util.Scanner;
import javax.management.Descriptor;

public class mirim {
    public static char alphabetBoard[][] = new char[5][5];  //암호판
    public static boolean oddFlag = false; //글자수 출력
    public static String zCheck ="";

    public static void main(String[] args) {

        String decryption;  //복호화
        String encryption;  //암호화

        Scanner sc = new Scanner(System.in); 	//입력을 위한 Scanner 정의
        System.out.print("암호화에 쓰일 키를 입력하세요 : ");
        String key = sc.nextLine().toUpperCase();				//key 입력, 들어오는 모든 값을 대문자로 변환
        System.out.print("암호화할 문자열을 입력하세요 : ");
        String originStr = sc.nextLine().toUpperCase();  //문자열 입력(원본)
        String str = originStr;  //공백없는 문자열
        String blankCheck="";
        int blankCheckCount=0;  //왜 사용하는거지?

        setBoard(key);							//암호화에 쓰일 암호판 세팅

        for( int i = 0 ; i < str.length() ; i++) {
            //공백제거
            if(str.charAt(i)==' ') {
                str = str.substring(0,i)+str.substring(i+1,str.length());   //공백 앞뒤로 잘라 붙여 공백 제거 후 str에 저장
                blankCheck+=10; //공백확인 +10
            }else {
                blankCheck+=0;  //공백이 없으면 0더함
            }

            //z를 q로 바꿔줘서 처리함.
            if(str.charAt(i)=='Z'){
                str = str.substring(0,i)+'Q'+str.substring(i+1,str.length());   //현재 위치로 하여 Z를 Q로 변경
                zCheck+=1;  //zcheck +1 추가
            }else {
                zCheck+=0;
            }
        }

        /*
        System.out.println("blankCheck : " + blankCheck);
        System.out.println("blankCheckCount : " + blankCheckCount);
         */
        
        //평문을 암호화한 값을 저장
        encryption = strEncryption(key, str);


        //출력부분(매핑된 문자열 출력)
        System.out.println("암호화된 문자열 : " + encryption);

        
        //2개씩 끊어 공백 제거후 encryption에 저장
        for( int i = 0 ; i < encryption.length() ; i++ )
        {
            if(encryption.charAt(i)==' ') //공백제거
                encryption = encryption.substring(0,i)+encryption.substring(i+1,encryption.length());
        }

        //공백없는 암호화된 문자열
        //System.out.println("암호화된 문자열(NoSpace) : " + encryption);
        
        //복호문계산
        decryption = strDecryption(key, encryption, zCheck);

        for( int i = 0 ; i < decryption.length() ; i++)
        {
            if(blankCheck.charAt(i)=='1')
            {
                decryption = decryption.substring(0,i)+" "+decryption.substring(i,decryption.length());
            }
        }

        System.out.println("복호화된 문자열 : " + decryption);

        //암호문이 복호화 잘 되었는지 원래 평문과 확인하여 출력
        if(decryption.equals(originStr)){
            System.out.println("SUCCESS");
        }else{
            System.out.println("FAIL");
        }
    }

    private static String strDecryption(String key, String str, String zCheck) {
        ArrayList<char[]> playFair = new ArrayList<char[]>(); //바꾸기 전 쌍자암호를 저장할 곳
        ArrayList<char[]> decPlayFair = new ArrayList<char[]>(); //바꾼 후의 쌍자암호 저장할 곳
        int x1 = 0 , x2 = 0 , y1 = 0, y2 = 0; //쌍자 암호 두 글자의 각각의 행,열 값
        String decStr ="";

        int lengthOddFlag = 1;

        for( int i = 0 ; i < str.length() ; i+=2 )
        {
            char[] tmpArr = new char[2];
            tmpArr[0] = str.charAt(i);
            tmpArr[1] = str.charAt(i+1);
            playFair.add(tmpArr);
        }


        for(int i = 0 ; i < playFair.size() ; i++ )
        {

            char[] tmpArr = new char[2];
            for( int j = 0 ; j < alphabetBoard.length ; j++ )
            {
                for( int k = 0 ; k < alphabetBoard[j].length ; k++ )
                {
                    if(alphabetBoard[j][k] == playFair.get(i)[0])
                    {
                        x1 = j;
                        y1 = k;
                    }
                    if(alphabetBoard[j][k] == playFair.get(i)[1])
                    {
                        x2 = j;
                        y2 = k;
                    }
                }
            }

            if(x1==x2) //행이 같은 경우 각각 바로 아래열 대입
            {
                tmpArr[0] = alphabetBoard[x1][(y1+4)%5];
                tmpArr[1] = alphabetBoard[x2][(y2+4)%5];
            }
            else if(y1==y2) //열이 같은 경우 각각 바로 옆 열 대입
            {
                tmpArr[0] = alphabetBoard[(x1+4)%5][y1];
                tmpArr[1] = alphabetBoard[(x2+4)%5][y2];
            }
            else //행, 열 다른경우 각자 대각선에 있는 곳.
            {
                tmpArr[0] = alphabetBoard[x2][y1];
                tmpArr[1] = alphabetBoard[x1][y2];
            }

            decPlayFair.add(tmpArr);

        }

        for(int i = 0 ; i < decPlayFair.size() ; i++) //중복 문자열 돌려놓음
        {
            if(i!=decPlayFair.size()-1 && decPlayFair.get(i)[1]=='x'
                    && decPlayFair.get(i)[0]==decPlayFair.get(i+1)[0])
            {
                decStr += decPlayFair.get(i)[0];
            }
            else
            {
                decStr += decPlayFair.get(i)[0]+""+decPlayFair.get(i)[1];
            }
        }



        for(int i = 0 ; i < zCheck.length() ; i++ )//z위치 찾아서 q로 돌려놓음
        {
            if( zCheck.charAt(i) == '1' )
                decStr = decStr.substring(0,i)+'z'+decStr.substring(i+1,decStr.length());

        }



        if(oddFlag) decStr = decStr.substring(0,decStr.length()-1);

		/*
		 //띄어쓰기
		for(int i = 0 ; i < decStr.length(); i++)
		{
			if(i%2==lengthOddFlag){
				decStr = decStr.substring(0, i+1)+" "+decStr.substring(i+1, decStr.length());
				i++;
				lengthOddFlag = ++lengthOddFlag %2;
			}
		}
		*/
        return decStr;
    }

    //암호판을 통해 평문 매핑하기
    private static String strEncryption(String key, String str){
        ArrayList<char[]> playFair = new ArrayList<char[]>();   //
        ArrayList<char[]> encPlayFair = new ArrayList<char[]>();    //암호문
        int x1 = 0 , x2 = 0 , y1 = 0, y2 = 0;
        String encStr ="";

        //암호키의 길이만큼 반복
        for( int i = 0 ; i < str.length() ; i+=2 ) // arraylist 세팅
        {
            char[] tmpArr = new char[2];
            tmpArr[0] = str.charAt(i);
            try{
                if(str.charAt(i) == str.charAt(i+1)) //글이 반복되면 x추가
                {
                    tmpArr[1] = 'X';    //매핑한 값 2개가 같으면 뒤쪽 값을 X로 변경
                    i--;                //i+2했을때 뒤쪽 값이 나올 수 있도록 i--
                }else{
                    tmpArr[1] = str.charAt(i+1);
                }
            }catch(StringIndexOutOfBoundsException e)
            {
                tmpArr[1] = 'X';
                oddFlag = true;
            }
            playFair.add(tmpArr);   //매핑된 값을 playFair에 크기2 배열로 추가
        }

        //playFair의 길이만큼 반복문
        /*
        for(int i = 0 ; i < playFair.size() ; i++)
        {
            //매핑된 값을 2개씩 끊어 출력
            System.out.print(playFair.get(i)[0]+""+playFair.get(i)[1]+" ");
        }
        System.out.println();
        */

        for(int i = 0 ; i < playFair.size() ; i++ )
        {
            char[] tmpArr = new char[2];
            for( int j = 0 ; j < alphabetBoard.length ; j++ ) //쌍자암호의 각각 위치체크
            {
                for( int k = 0 ; k < alphabetBoard[j].length ; k++ )
                {
                    //암호테이블에서 매핑한 두개의 알파벳과 같은 알파벳의 위치를 저장
                    if(alphabetBoard[j][k] == playFair.get(i)[0])
                    {
                        x1 = j;
                        y1 = k;
                    }
                    if(alphabetBoard[j][k] == playFair.get(i)[1])
                    {
                        x2 = j;
                        y2 = k;
                    }
                }
            }


            if(x1==x2) //행이 같은경우
            {
                tmpArr[0] = alphabetBoard[x1][(y1+1)%5];
                tmpArr[1] = alphabetBoard[x2][(y2+1)%5];
            }
            else if(y1==y2) //열이 같은 경우
            {
                tmpArr[0] = alphabetBoard[(x1+1)%5][y1];
                tmpArr[1] = alphabetBoard[(x2+1)%5][y2];
            }
            else //행, 열 모두 다른경우
            {
                tmpArr[0] = alphabetBoard[x2][y1];
                tmpArr[1] = alphabetBoard[x1][y2];
            }

            //계산 후, 나온 암호문 encPlayFair에 추가
            encPlayFair.add(tmpArr);

        }   //playFair 배열이 끝날때까지 반복

        //ArrayList를 2개씩 붙여 문자열로 전환
        for(int i = 0 ; i < encPlayFair.size() ; i++)
        {
            encStr += encPlayFair.get(i)[0]+""+encPlayFair.get(i)[1]+" ";
        }

        //encStr은 암호화된 문자열을 뜻함
        //System.out.println(encStr);

        return encStr;
    }

    //암호키를 이용해 암호판 세팅하기
    private static void setBoard(String key) {
        String keyForSet = "";					// 중복된 문자가 제거된 문자열을 저장할 문자열.
        boolean duplicationFlag = false;		// 문자 중복을 체크하기 위한 flag 변수.
        int keyLengthCount = 0;					// alphabetBoard에 keyForSet을 넣기 위한 count변수.

        key += "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 	// 키에 모든 알파벳을 추가.


        // 중복처리
        for( int i = 0 ; i < key.length() ; i++ ) {
            for( int j = 0 ; j < keyForSet.length() ; j++ ) {
                if(key.charAt(i)==keyForSet.charAt(j))
                {
                    duplicationFlag = true;
                    break;
                }
            }
            if(!(duplicationFlag)) keyForSet += key.charAt(i);  //만약 중복문자가 아니면 keyforSet에 현재 값을 넣어
            duplicationFlag = false;    //duplicationFlag는 다시 false로 초기화
        }

        //중복된 문자가 제거된 문자열 출력 확인
        //System.out.println(keyForSet);
        
        //배열에 대입
        for( int i = 0 ; i < alphabetBoard.length ; i++ )
        {
            for( int j = 0; j <alphabetBoard[i].length ; j++ )
            {
                alphabetBoard[i][j] = keyForSet.charAt(keyLengthCount++);
            }
        }
        //배열 내용 출력
        for( int i = 0 ; i < alphabetBoard.length ; i++ )
        {
            for( int j = 0; j <alphabetBoard[i].length ; j++ )
            {
                System.out.print(alphabetBoard[i][j]+" ");
            }
            System.out.println();
        }


    }
}
