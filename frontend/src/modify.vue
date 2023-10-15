<script setup>
// created 라이프사이클 훅에서 API 요청
import { onMounted, ref } from "vue";
import axios from "axios";
import router from "@/router";

// data 속성 정의
const modifyBoardContent = ref(null)
// eslint-disable-next-line no-unused-vars
let viewListFile = []

//가져온 content
const modifyContent = async () => {
  try {
    // const response = await $axios.get(`${$serverUrl}/index`); // 실제 서버 주소로 변경해야 합니다.
    const response = await axios.get(`http://localhost:8082/modify`) // 실제 서버 주소로 변경해야 합니다.
    modifyBoardContent.value = response.data.modifyBoardContent
    viewListFile = response.data.viewListFile

    categoryNum.value = modifyBoardContent.value.categoryNum
    writer.value = modifyBoardContent.value.writer
    pw.value = modifyBoardContent.value.pw
    title.value = modifyBoardContent.value.title
    content.value = modifyBoardContent.value.content

  } catch (error) {
    console.error('전송 에러:', error)
  }
}

onMounted(async () => {
  modifyContent
});

//수정할 content
const categoryNum = ref('');
const writer = ref('');
const pw = ref('');
const title = ref('');
const content = ref('');

const submitForm = async () => {
  try {
    await axios.post(`http://localhost:8082/modify`, {
      categoryNum: categoryNum.value,
      writer: writer.value,
      pw: pw.value,
      title: title.value,
      content: content.value
    })
    router.push({ name: 'content', params: {boardNum : viewListFile.boardNum} })
  } catch (error) {
    console.error('전송 에러:', error)
  }
}

</script>


<template>
  <div>
  <h1>게시판 - 수정</h1>
  <br/>

  <div style="margin-top: 1%; align-content: center; align-items: center; text-align: center">
    <form @submit.prevent="submitForm" enctype="multipart/form-data">
      <table style= "width:80%;">
        <tr>
          <td style="color: black; font-weight: bolder;">
            카테고리&nbsp;
            <select v-model="categoryNum">
              <option disabled value="">카테고리 선택</option>
              <option value="1">JAVA</option>
              <option value="2">JS</option>
              <option value="3">SpringBoot</option>
              <option value="4">Android</option>
            </select>
          </td>
        </tr>
        <tr>
          <td style="color: black; font-weight: bolder;">
            등록일시 {{modifyBoardContent.writeDate}}
          </td>
        </tr>
        <tr>
          <td style="color: black; font-weight: bolder;">
            수정일시 {{modifyBoardContent.modifyDate}}
          </td>
        </tr>
        <tr>
          <td style="color: black; font-weight: bolder;">
            조회수&nbsp;&nbsp;&nbsp;&nbsp;{{modifyBoardContent.hit}}
          </td>
        </tr>
        <tr>
          <td style="color: black; font-weight: bolder;">
            작성자&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" v-model="writer">
          </td>
        </tr>
        <tr>
          <td style="color: black; font-weight: bolder;">
            비밀번호&nbsp;<input type="password" v-model="pw">
          </td>
        </tr>
        <tr>
          <td style="color: black; font-weight: bolder;">
            글제목&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" v-model="title">
          </td>
        </tr>
        <tr>
          <td style="color: black; font-weight: bolder;">
            글내용<textarea rows="12" style="width:100%;" class="click" name="content" v-model="content" />
          </td>
        </tr>

        <tr>
          <td style="color: black; font-weight: bolder;">
            파일첨부
            <br>
                <a href="${pageContext.request.contextPath}/fileDelete?boardNum=${viewFileList.boardNum}&fileNum=${viewFileList.fileNum}" style="text-decoration:none; color:black">

                </a>
                fileName<br>
            <input type="file" id="file" name="file"><br>
            <input type="file" id="file1" name="file1"><br>
            <input type="file" id="file2" name="file2"><br>
          </td>
        </tr>

        <tr style="margin-bottom: 5%">
          <!-- 글 수정 메뉴 -->
          <td colspan="2" align="center" style="margin-bottom: 5%">
            <button @click.prevent="router.go(-1)">취소</button>
            <input type="submit" value="저장">
          </td>
        </tr>
      </table>
    </form>
  </div>
  </div>
</template>