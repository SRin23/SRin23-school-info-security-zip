package com.srin23.playfair.service;

import com.srin23.playfair.domain.repository.PlayfairRepository;
import com.srin23.playfair.domain.repository.dto.response.DelSpaceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class PlayfairService{
    private final PlayfairRepository playfairRepository;

    public static boolean oddFlag = false; //글자수 출력


    @Transactional
    public char[][] setBoard(String key) {  //보드만들기
        char alphabetBoard[][] = new char[5][5];  //암호판
        String keyForSet = "";					// 중복된 문자가 제거된 문자열을 저장할 문자열.
        boolean duplicationFlag = false;		// 문자 중복을 체크하기 위한 flag 변수.
        int keyLengthCount = 0;					// alphabetBoard에 keyForSet을 넣기 위한 count변수.

        key += "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 	// 키에 모든 알파벳을 추가.


        // 중복처리
        for( int i = 0 ; i < key.length() ; i++ ) {
            for( int j = 0 ; j < keyForSet.length() ; j++ ) {
                if(key.charAt(i)==keyForSet.charAt(j)) {
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
        for( int i = 0 ; i < alphabetBoard.length ; i++ ) {
            for( int j = 0; j <alphabetBoard[i].length ; j++ ) {
                alphabetBoard[i][j] = keyForSet.charAt(keyLengthCount++);
            }
        }

        return alphabetBoard;
    }

    @Transactional
    public DelSpaceDto DelSpace(String str){
        String zCheck ="";
        String blankCheck="";
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
                zCheck+=1;
            }else {
                zCheck+=0;
            }
        }
        DelSpaceDto delSpaceDto = new DelSpaceDto(zCheck, blankCheck, str);
        return delSpaceDto;
    }

    @Transactional
    public String RealEncryption(String encryption){
        //2개씩 끊어 공백 제거후 encryption에 저장
        for( int i = 0 ; i < encryption.length() ; i++ ) {
            if(encryption.charAt(i)==' ') //공백제거
                encryption = encryption.substring(0,i)+encryption.substring(i+1,encryption.length());
        }
        return encryption;
    }

    @Transactional
    public String realDecryption(String blankCheck, String decryption){
        for( int i = 0 ; i < blankCheck.length() ; i++) {
            if(blankCheck.charAt(i)=='1') {
                decryption = decryption.substring(0,i)+" "+decryption.substring(i,decryption.length());
            }
        }
        return decryption;
    }

    @Transactional
    public boolean finalCheck(String text, String decryption){
        if(decryption.equals(text)){
            return true;
        }else{
            return false;
        }
    }

    @Transactional
    public String strEncryption(char[][] alphabetBoard, String key, String str){    //암호화
        ArrayList<char[]> playFair = new ArrayList<char[]>();   //
        ArrayList<char[]> encPlayFair = new ArrayList<char[]>();    //암호문
        int x1 = 0 , x2 = 0 , y1 = 0, y2 = 0;
        String encStr ="";

        //암호키의 길이만큼 반복
        for( int i = 0 ; i < str.length() ; i+=2 ){
            char[] tmpArr = new char[2];
            tmpArr[0] = str.charAt(i);
            try{
                if(str.charAt(i) == str.charAt(i+1)){
                    tmpArr[1] = 'X';    //매핑한 값 2개가 같으면 뒤쪽 값을 X로 변경
                    i--;                //i+2했을때 뒤쪽 값이 나올 수 있도록 i--
                }else{
                    tmpArr[1] = str.charAt(i+1);
                }
            }catch(StringIndexOutOfBoundsException e) {
                tmpArr[1] = 'X';
                oddFlag = true;
            }
            playFair.add(tmpArr);   //매핑된 값을 playFair에 크기2 배열로 추가
        }

        for(int i = 0 ; i < playFair.size() ; i++ ) {
            char[] tmpArr = new char[2];
            for( int j = 0 ; j < alphabetBoard.length ; j++ ) {
                for( int k = 0 ; k < alphabetBoard[j].length ; k++ ) {
                    //암호테이블에서 매핑한 두개의 알파벳과 같은 알파벳의 위치를 저장
                    if(alphabetBoard[j][k] == playFair.get(i)[0]) {
                        x1 = j;
                        y1 = k;
                    }
                    if(alphabetBoard[j][k] == playFair.get(i)[1]) {
                        x2 = j;
                        y2 = k;
                    }
                }
            }


            if(x1==x2){
                tmpArr[0] = alphabetBoard[x1][(y1+1)%5];
                tmpArr[1] = alphabetBoard[x2][(y2+1)%5];
            }
            else if(y1==y2) {
                tmpArr[0] = alphabetBoard[(x1+1)%5][y1];
                tmpArr[1] = alphabetBoard[(x2+1)%5][y2];
            }
            else{
                tmpArr[0] = alphabetBoard[x2][y1];
                tmpArr[1] = alphabetBoard[x1][y2];
            }

            encPlayFair.add(tmpArr);

        }

        for(int i = 0 ; i < encPlayFair.size() ; i++){
            encStr += encPlayFair.get(i)[0]+""+encPlayFair.get(i)[1]+" ";
        }

        return encStr;
    }

    @Transactional
    public String strDecryption(char[][] alphabetBoard, String key, String str, String zCheck) {
        ArrayList<char[]> playFair = new ArrayList<char[]>(); //바꾸기 전 쌍자암호를 저장할 곳
        ArrayList<char[]> decPlayFair = new ArrayList<char[]>(); //바꾼 후의 쌍자암호 저장할 곳
        int x1 = 0 , x2 = 0 , y1 = 0, y2 = 0; //쌍자 암호 두 글자의 각각의 행,열 값
        String decStr ="";

        int lengthOddFlag = 1;

        for( int i = 0 ; i < str.length() ; i+=2 ) {
            char[] tmpArr = new char[2];
            tmpArr[0] = str.charAt(i);
            tmpArr[1] = str.charAt(i+1);
            playFair.add(tmpArr);
        }

        for(int i = 0 ; i < playFair.size() ; i++ ) {

            char[] tmpArr = new char[2];
            for( int j = 0 ; j < alphabetBoard.length ; j++ ) {
                for( int k = 0 ; k < alphabetBoard[j].length ; k++ ) {
                    if(alphabetBoard[j][k] == playFair.get(i)[0]){
                        x1 = j;
                        y1 = k;
                    }
                    if(alphabetBoard[j][k] == playFair.get(i)[1]) {
                        x2 = j;
                        y2 = k;
                    }
                }
            }

            if(x1==x2){
                tmpArr[0] = alphabetBoard[x1][(y1+4)%5];
                tmpArr[1] = alphabetBoard[x2][(y2+4)%5];
            }else if(y1==y2){
                tmpArr[0] = alphabetBoard[(x1+4)%5][y1];
                tmpArr[1] = alphabetBoard[(x2+4)%5][y2];
            }else {
                tmpArr[0] = alphabetBoard[x2][y1];
                tmpArr[1] = alphabetBoard[x1][y2];
            }
            decPlayFair.add(tmpArr);
        }

        for(int i = 0 ; i < decPlayFair.size() ; i++){
            if(i!=decPlayFair.size()-1 && decPlayFair.get(i)[1]=='x'&& decPlayFair.get(i)[0]==decPlayFair.get(i+1)[0]) {
                decStr += decPlayFair.get(i)[0];
            } else {
                decStr += decPlayFair.get(i)[0]+""+decPlayFair.get(i)[1];
            }
        }



        for(int i = 0 ; i < zCheck.length() ; i++ ){
            if( zCheck.charAt(i) == '1' ) decStr = decStr.substring(0,i)+'z'+decStr.substring(i+1,decStr.length());
        }

        if(oddFlag) decStr = decStr.substring(0,decStr.length()-1);

        return decStr;
    }
}
