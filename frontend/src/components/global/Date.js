/**
 * ISO 8601 날짜 문자열을 "YYYY-MM-DD HH:MM" 형식으로 변환합니다.
 * @param {string} dateString - '2025-11-18T10:08:03.103352' 형태의 문자열
 * @returns {string} - '2025-11-18 10:08' 형태의 문자열
 */
export function formatDateTimeMinute(dateString) {
    if (!dateString) return '-';

    try {
        const date = new Date(dateString);

        // 날짜 구성 요소 추출
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
        const day = String(date.getDate()).padStart(2, '0');
        const hour = String(date.getHours()).padStart(2, '0');
        const minute = String(date.getMinutes()).padStart(2, '0');

        // 최종 형식으로 조합
        return `${year}-${month}-${day} ${hour}:${minute}`;

    } catch (error) {
        console.error("날짜 포맷팅 오류:", error);
        return dateString; // 오류 발생 시 원본 문자열 반환
    }
}