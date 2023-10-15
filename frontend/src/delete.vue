<script setup>

import { onMounted, ref } from "vue";
import axios from "axios";

const pw = ref('');
const deleteContent = ref(null);

//비밀번호 불러오기
onMounted(async () => {
  try {

    const response = await axios.get('http://localhost:8082/delete')
    deleteContent.value = response.data.deleteContent;

  } catch (error){
    console.log('전송 에러: ', error)
  }
});

//불러온 비밀번호와 비교후 삭제
const checkPw = async () => {
  if(pw.value === deleteContent.value.pw){
    alert('게시물이 삭제되었슺니다.');

    try {

      await axios.post('http://localhost:8082/delete', { boardNum : deleteContent.value.boardNum });
    } catch (error) {
      console.log('삭제 실패: ',error)
    }
  } else {
    alert('비밀번호가 일치하지 않습니다.')
  }
};
</script>

<template>
  <div style="display: flex; justify-content: center; align-items: center; margin: 20%">
    <article style="text-align: center; border: 3px solid #dddddd; width: 40%">
      <form @submit.prevent="checkPw">
        <table >
          <h3 >비밀번호를 입력하세요</h3><br>
          <input type="password" v-model="pw" placeholder="비밀번호를 입력하세요" required="required">
          <input type="submit" value="삭제">
        </table>
      </form>
    </article>
  </div>
</template>