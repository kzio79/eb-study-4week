<script setup>
// created 라이프사이클 훅에서 API 요청
import { onMounted, ref } from 'vue'
import axios from 'axios'
import router from '@/router'

// data 속성 정의
const viewBoardContent = ref(null)
const viewReplyContent = ref(null)
// eslint-disable-next-line no-unused-vars
let viewListFile = []

onMounted(async () => {
  try {
    // const response = await $axios.get(`${$serverUrl}/index`); // 실제 서버 주소로 변경해야 합니다.
    const response = await axios.get(`http://localhost:8082/content`) // 실제 서버 주소로 변경해야 합니다.
    viewBoardContent.value = response.data.viewBoardContent
    viewReplyContent.value = response.data.viewReplyContent
    viewListFile = response.data.viewListFile
  } catch (error) {
    console.error('전송 에러:', error)
  }
})

const pageIndex = () => {
  router.push({ name: 'index' })
}

const pageModify = () => {
  // eslint-disable-next-line no-undef
  router.push({ name: 'modify', params: { boardNum } })
}

const pageDelete = () => {
  // eslint-disable-next-line no-undef
  router.push({ name: 'delete', params: { boardNum } })
}
</script>

<template>
  <div>
    <h1>게시판 - 보기</h1>
    <div align="center" style="margin-top: 2%; width: 80%">
      <div style="display: flex; justify-content: space-between; margin-left: 10%">
        <td name="writer" style="color: black; font-weight: bolder" aria-readonly="true">
          {{ viewBoardContent.writer }}}
        </td>
        <div>
          <td
            name="writeDate"
            style="align-items: end; color: black; font-weight: bolder"
            aria-readonly="true"
          >
            등록일시 {{ viewBoardContent.writeDate }}
          </td>

          <td name="modifyDate" style="color: black; font-weight: bolder" aria-readonly="true">
            수정일시 {{ viewBoardContent.modifyDate }}
          </td>
        </div>
      </div>
      <div style="display: flex; justify-content: space-between; margin-left: 10%">
        <td name="categoryNum" style="color: black; font-weight: bolder" aria-readonly="true">
          [ JAVA JS SpringBoot Android ]${viewBoardContent.title}
        </td>
        <div>
          <td name="hit" style="color: black; font-weight: bolder" aria-readonly="true">
            조회수 {{ viewBoardContent.hit }}
          </td>
        </div>
      </div>
    </div>
    <hr />
    <div>
      <div align="center" style="margin-top: 1%">
        <form
          action="${pageContext.request.contextPath}/modify"
          name="regform"
          style="margin-bottom: 5%; margin-top: 2%"
        >
          <table style="width: 80%">
            <tr>
              <td style="color: black; font-weight: bolder">
                <textarea
                  rows="12"
                  style="width: 100%"
                  class="click"
                  name="content"
                  v-model="viewBoardContent.content"
                />
              </td>
            </tr>

            <!--            fileView, fileDownload-->
            <tr>
              <ul>
                <td style="color: black; font-weight: bolder">
                  <br />
                  등록된 파일
                  <br />

                  <a
                    href="/download?boardNum=&fileNum="
                    style="text-decoration: none; color: black"
                  >
                    <!--                        <img src="/images/file.jpg" width="1.5%">-->
                  </a>
                  {{ viewListFile }}<br />
                </td>
              </ul>
            </tr>

            <div align="center">
              <button @click="pageIndex">목록</button>
              <button @click="pageModify">수정</button>
              <button @click="pageDelete">삭제</button>
            </div>
          </table>
        </form>

        <!--        댓글부분-->
        <div>
          <div align="center" style="margin: 2%">
            <form
              id="reply"
              action="/content?boardNum="
              method="post"
              onsubmit="return replyNull()"
            >
              <input type="hidden" name="boardNum" value="${viewBoardContent.boardNum}" />
              <table style="border: 1px solid #dddddd; width: 80%">
                <tr>
                  <td style="border-bottom: none" valign="middle"></td>
                  <td style="color: black; font-weight: bolder" id="replyList">
                    {{ viewReplyContent.replyDate }}<br />
                    {{ viewReplyContent.content }}
                    <hr />
                  </td>
                </tr>
                <tr>
                  <td style="border-bottom: none" valign="middle"></td>
                  <td style="color: black; font-weight: bolder">
                    <textarea
                      rows="2"
                      style="width: 100%"
                      name="replyContent"
                      id="replyContent"
                      placeholder="댓글을 입력해 주세요"
                    ></textarea>
                  </td>
                  <td>
                    <input
                      type="hidden"
                      name="boardNum"
                      id="boardNum"
                      value="${viewBoardContent.boardNum}"
                    />
                    <input type="submit" class="btn btn-primary pull" value="등록" />
                  </td>
                </tr>
              </table>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>