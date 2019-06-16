# Lost and Find
## Description
분실물과 습득물을 등록하여 사용자들이 쉽게 찾을 수 있다.  
분실 위치를 지도에 출력하여 보다 좋은 인터페이스를 제공한다.

## Tools
- Android Studio
- Sourcetree
- Atom

## 기능 설명
##### [AuthActivity.java]
- 이메일 인증 클래스

##### [CreateBoardActivity.java]
- 게시글 작성 클래스

##### [CreateBoardApiAsevice.java]
- multer를 이용하여 서버에 이미지 업로드를 위한 인터페이스 클래스

##### [FileCache.java]
- 갤러리에서 이미지 끌어올 때 SD카드 사용여부 클래스

##### [FinidActivity.java]
- 아이디 찾기 클래스

##### [ResetpwActivity.java]
- 비밀번호 재설정 클래스

##### [GpsInfo.java]
- GPS이용, 현재 gps값 가져오는 클래스

##### [HomeActivity.java]
- 분실물, 습득물, 조회, 내가쓴글사용자 출력 클래스

##### [ImageLoader.java]
- URL로 된 이미지 불러올때 사용하는 클래스

##### [LoginActivity.java]
- 로그인 클래스

##### [MapsActivity.java]
- 게시글 작성할때  지도 출력하는 클래스

##### [MemoryCache.java]
- 이미지 불러올때 저장소 접근 클래스

##### [PrintMapMarker.java]
- 홈에서 분실물 또는 습득물 선택 시 지도 출력해서 사용자에게 도시 위치값 받는 클래스

##### [ReplyView.java]
- 서버와 통신시 댓글 값 저장 하는 클래스

##### [ReplypwActivity.java]
- ReplyView에서 받은 값 출력하는 클래스

##### [SignActivity.java]
- 회원가입 클래스

##### [myWriter.java]
- 자기가 작성한 글 서버와 통신후 값 저장 클래스

##### [myWriterAdapterView.java]
- 작성한 글 출력 클래스

##### [SearchResult.java]
- 검색한거 서버와 통신 후 데이터 값 저장하는 클래스

##### [SearchResultAdapter.java]
- 검색값 화면에 출력 클래스

##### [TypeView.java]
- 분실물 습득물 서버와 통신후 값 저장하는 클래스

##### [TypeViewAdapter.java]
- TyperView의 값을 화면에 출력하는 클래스
