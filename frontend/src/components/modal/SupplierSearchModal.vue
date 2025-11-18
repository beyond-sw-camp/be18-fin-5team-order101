<script setup>
import { ref, watch } from 'vue';
import axios from 'axios';

const props = defineProps({
    isOpen: { type: Boolean, default: false },
    selectedSupplier: { type: Object, default: null }
});
const emit = defineEmits(['update:isOpen', 'select']);

const suppliers = ref([]);
const totalCount = ref(0);
const currentPage = ref(1);
const pageSize = ref(10);
const keyword = ref('');
const isLoading = ref(false);
const internalSelectedSupplier = ref(props.selectedSupplier);

// API 통신 로직
const fetchSuppliers = async (page = 1) => {
    isLoading.value = true;
    try {
        const response = await axios.get('/api/v1/suppliers', {
            params: { page: page, numOfRows: pageSize.value, keyword: keyword.value }
        });
        const responseData = response.data; // ItemsResponseDto 객체

        // 1. 목록 (List<SupplierListRes>): DTO의 'data' 필드에 실제 리스트가 들어있음
        suppliers.value = responseData.items || [];

        // 2. 전체 개수: DTO의 'totalCount' 필드 사용
        totalCount.value = responseData.totalCount;

        // 3. 현재 페이지: DTO의 'page' 필드 사용
        currentPage.value = responseData.page;

    } catch (error) {
        console.error("공급업체 목록 조회 실패:", error);
        alert("공급업체 목록을 불러오는 데 실패했습니다.");
    } finally {
        isLoading.value = false;
    }
};

const handleSearch = () => fetchSuppliers(1);
const handlePageChange = (newPage) => fetchSuppliers(newPage);
const selectSupplier = (supplier) => internalSelectedSupplier.value = supplier;

const confirmSelection = () => {
    if (internalSelectedSupplier.value) {
        emit('select', internalSelectedSupplier.value);
    }
    closeModal();
};

const closeModal = () => emit('update:isOpen', false);

watch(() => props.isOpen, (newVal) => {
    if (newVal) {
        internalSelectedSupplier.value = props.selectedSupplier;
        fetchSuppliers(1);
    }
});
</script>

<template>
    <div v-if="isOpen" class="modal-overlay" @click.self="closeModal">
        <div class="modal-content">

            <div class="modal-header">
                <h3>공급업체 선택</h3>
                <button @click="closeModal" class="modal-close-button">&times;</button>
            </div>

            <div class="search-area">
                <input v-model="keyword" @keyup.enter="handleSearch" placeholder="검색 키워드 입력" class="search-input" />
                <button @click="handleSearch" class="btn-primary">조회</button>
            </div>

            <div class="supplier-list-container">
                <div v-if="isLoading" class="loading-spinner">목록을 불러오는 중...</div>
                <table v-else class="supplier-table">
                    <thead>
                        <tr>
                            <th>선택</th>
                            <th>ID</th>
                            <th>공급업체명</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="s in suppliers" :key="s.supplierId"
                            :class="{ selected: internalSelectedSupplier && internalSelectedSupplier.supplierId === s.supplierId }"
                            @click="selectSupplier(s)">
                            <td><input type="radio"
                                    :checked="internalSelectedSupplier && internalSelectedSupplier.supplierId === s.supplierId"
                                    @change="selectSupplier(s)"></td>
                            <td>{{ s.supplierId }}</td>
                            <td>{{ s.supplierName }}</td>
                        </tr>
                        <tr v-if="!isLoading && suppliers.length === 0">
                            <td colspan="3" class="empty-data">조회된 공급업체가 없습니다.</td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <div class="modal-footer">
                <button @click="closeModal" class="btn-secondary">취소</button>
                <button @click="confirmSelection" class="btn-primary confirm">선택 완료</button>
            </div>
        </div>
    </div>
</template>
<style scoped>
/* 1. 기본 구조 및 모달 스타일 */
.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.4);
    /* 배경 투명도 약간 낮춤 */
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
}

.modal-content {
    background: white;
    padding: 30px;
    /* 패딩 증가 */
    border-radius: 12px;
    /* 모서리 부드럽게 */
    width: 650px;
    /* 너비 약간 확대 */
    /* 최신 트렌드: 부드러운 그림자 (Elevation) */
    box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1), 0 5px 10px rgba(0, 0, 0, 0.05);
    max-height: 80vh;
    display: flex;
    flex-direction: column;
}

/* 2. 헤더 및 닫기 버튼 */
.modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-bottom: 15px;
    border-bottom: 1px solid #e0e0e0;
    /* 연한 구분선 */
    margin-bottom: 20px;
}

.modal-header h3 {
    font-size: 1.5rem;
    color: #333;
    font-weight: 600;
}

/* X 닫기 버튼 스타일 */
.modal-close-button {
    background: none;
    border: none;
    font-size: 28px;
    /* 크기 약간 키움 */
    color: #999;
    cursor: pointer;
    padding: 0;
    line-height: 1;
    transition: color 0.2s ease;
}

.modal-close-button:hover {
    color: #666;
    /* 호버 시 진한 회색 */
}

/* 3. 검색 영역 */
.search-area {
    display: flex;
    gap: 10px;
    margin-bottom: 20px;
}

.search-input {
    flex-grow: 1;
    padding: 10px 12px;
    border-radius: 6px;
    border: 1px solid #d1d5db;
    /* 깔끔한 경계선 */
    font-size: 1rem;
    transition: border-color 0.2s;
}

.search-input:focus {
    border-color: #4f46e5;
    /* 포커스 시 강조 색상 */
    outline: none;
}

/* 4. 목록 테이블 */
.supplier-list-container {
    overflow-y: auto;
    /* 목록이 길어지면 스크롤 가능 */
    flex-grow: 1;
    margin-bottom: 20px;
}

.loading-spinner {
    text-align: center;
    padding: 40px;
    color: #999;
}

.supplier-table {
    width: 100%;
    border-collapse: collapse;
}

.supplier-table th {
    background-color: #f9fafb;
    /* 헤더 배경색 */
    font-weight: 600;
    color: #4b5563;
    padding: 12px 8px;
    border-bottom: 1px solid #e0e0e0;
}

.supplier-table td {
    padding: 12px 8px;
    border-bottom: 1px solid #f3f4f6;
    cursor: pointer;
    transition: background-color 0.15s;
}

.supplier-table tr:hover {
    background-color: #f9fafb;
}

/* 중앙 정렬 유지 및 너비 조정 */
.supplier-table th:nth-child(1),
.supplier-table td:nth-child(1) {
    width: 50px;
    text-align: center;
}

/* 선택 */
.supplier-table th:nth-child(2),
.supplier-table td:nth-child(2) {
    width: 100px;
    text-align: center;
}

/* ID */
.supplier-table th:nth-child(3),
.supplier-table td:nth-child(3) {
    text-align: left;
    padding-left: 20px;
}

/* 공급업체명은 왼쪽 정렬이 더 가독성 높음 */

/* 선택된 행 스타일 */
.supplier-table tr.selected {
    background-color: #eff6ff;
    /* 연한 파란색 계열 */
    border-left: 3px solid #3b82f6;
    /* 선택 강조 */
}

/* 라디오 버튼 스타일 (선택 시 자동으로 적용되므로 CSS는 최소화) */
.supplier-table td input[type="radio"] {
    cursor: pointer;
}

.empty-data {
    text-align: center;
    color: #9ca3af;
    padding: 24px;
}

/* 5. 푸터 및 버튼 */
.modal-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
    padding-top: 15px;
    border-top: 1px solid #e0e0e0;
    /* 푸터 상단 구분선 */
    margin-top: 20px;
}

/* 기본 버튼 스타일 */
.btn-primary,
.btn-secondary {
    padding: 10px 18px;
    border-radius: 6px;
    cursor: pointer;
    font-weight: 600;
    transition: all 0.2s;
}

/* 메인 버튼 (조회, 선택 완료) */
.btn-primary {
    background-color: #4f46e5;
    /* 메인 강조 색상 */
    color: white;
    border: none;
}

.btn-primary:hover {
    background-color: #4338ca;
}

/* 취소 버튼 (보조 버튼) */
.btn-secondary {
    background: white;
    color: #4b5563;
    border: 1px solid #d1d5db;
}

.btn-secondary:hover {
    background-color: #f9fafb;
    border-color: #9ca3af;
}

/* 선택 완료 버튼은 Primary 스타일 사용 */
.btn-primary.confirm {
    background-color: #4f46e5;
}
</style>