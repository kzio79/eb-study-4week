<script setup>
// created 라이프사이클 훅에서 API 요청
import { ref } from "vue";
import axios from "axios";
import router from "@/router";

const categoryNum = ref('');
const writer = ref('');
const pw = ref('');
const pwCheck = ref('');
const title = ref('');
const content = ref('');

const submitForm = async ()=> {
  if(pw.value !== pwCheck.value){
    alert('비밀번호가 일치하지 않습니다.')
  }

  try {
    await axios.post(`http://localhost:8082/write`, {
      categoryNum: categoryNum.value,
      writer: writer.value,
      pw: pw.value,
      title: title.value,
      content: content.value
    })
    router.push({ name: 'index' })
  } catch (error) {
    console.error('전송 에러:', error)
  }
}

const pageIndex = () => {
  router.push({ name: 'index' })
}



</script>

<template>
  <div style="align-items: start">
    <h1>자유게시판 - 글작성</h1><br/>
     <div style="margin-top: 2%; margin-left:5%;" >
      <div align="center"  style="margin-top: 5%;">
        <form @submit.prevent="submitForm" enctype="multipart/form-data" style="margin-bottom: 5%; margin-top: 2%" method="post" >
          <table style= "width:80%;">
            <tr>
              <td style="color: black; font-weight: bolder;">
                카테고리&nbsp;<select id="categoryNum" name="categoryNum" required>
                <option value="">카테고리 선택</option>
                <option value="1">JAVA</option>
                <option value="2">JS</option>
                <option value="3">SpringBoot</option>
                <option value="4">Android</option>
               </select>
              </td>
            </tr>
            <tr>
              <td style="color: black; font-weight: bolder;">
                작성자&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="writer" id="writer" required></td>
            </tr>
            <tr>
              <td style="color: black; font-weight: bolder;">
                비밀번호&nbsp;<input type="password" name="pw" id="pw" placeholder="비밀번호" required>
                <input type="password" name="pwCheck" id="pwCheck" placeholder="비밀번호확인" required></td>
            </tr>
            <tr>
              <td style="color: black; font-weight: bolder;">
                글제목&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="title" id="title" style="width: 90%;" class="click" required></td>
            </tr>
            <tr>
              <td style="color: black; font-weight: bolder;">
                글내용<textarea rows="12" style="width:100%;" class="click" name="content" id="content" required></textarea>
              </td>
            </tr>
            <tr>
              <td style="color: black; font-weight: bolder;">

                <input type="file" id="file" name="file" ><br>
                <input type="file" id="file1" name="file1" ><br>
                <input type="file" id="file2" name="file2" ><br>
              </td>
            </tr>
            <tr style="margin-bottom: 5%">
              <td colspan="2" align="center" style="margin-bottom: 5%">
                <button @click.prevent="pageIndex">취소</button>
                <input type="submit" value="저장" >
              </td>
            </tr>
          </table>
        </form>
      </div>
    </div>
  </div>
</template>