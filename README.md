# portfolio-1
> 자바 Spring Boot + JPA 로 작업한 토이 프로젝트 입니다.<br><br>
> Spring과 JPA 관련 강의를 듣고 이를 사용한 프로젝트를 만들어 봄으로써 스스로 부족한 부분을 인지하고 개선하는 것에 목적을 두었습니다.<br><br>
> 상품, 주문에 관련된 관리자 페이지를 만들어 토이 프로젝트에서 그치치 않고 하나의 템플릿화를 하려고 합니다.<br>


### 1. 요구사항 분석
<ol>
  <li>회원
    <ul>
      <li>회원 가입</li>
      <li>로그인, 로그아웃</li>
      <li>회원 등록, 수정, 삭제, 목록, 상세</li>
      <li>회원의 등급은 ADMIN(관리자), USER(일반 유저)로 나눠져 있고 일반 유저는 관리 메뉴에는 접근할 수 없어야 한다.</li>
    </ul>
  </li>
  <li>상품
    <ul>
      <li>카테고리 등록, 수정, 삭제, 상품과 카테고리 연결</li>
      <li>상품 등록(이미지 업로드 포함), 수정, 삭제, 상세</li>
      <li>상품 목록(관리자)</li>
      <li>상품 목록(일반 유저)</li>
      <li>상품 등록시 상품의 노출 여부를 선택할 수 있어야 한다.</li>
      <li>일반 유저용 상품 목록은 노출 상태의 상품만 나타나야 한다.</li>
      <li>상품 등록시 옵션도 같이 등록돼야 한다.</li>
      <li>상품은 옵션별로 재고 관리가 돼야 한다.</li>
      <li>상품 수정시 이미지 추가 등록, 개별 삭제가 가능해야 한다.</li>
      <li>상품 수정시 옵션 추가 등록, 개별 삭제가 가능해야 한다.</li>
    </ul>
  </li>
  <li>주문
    <ul>
      <li>주문 등록, 본인의 주문 목록</li>
      <li>주문 목록(관리자)</li>
      <li>주문시 구입 개수만큼 해당 옵션의 상품 재고가 감소해야 한다.</li>
      <li>주문 취소가 가능해야 하고, 주문 취소시 상품 재고가 복원돼야 한다.</li>
      <li>관리자는 주문 건에 대해서 배송 완료 상태로 변경시킬 수 있어야 한다.</li>
      <li>이미 배송 완료가 된 주문 건은 주문 취소가 불가능 해야한다.</li>
    </ul>
  </li>
</ol>

