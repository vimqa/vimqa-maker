import { CompactQaSample } from '../model/CompactQaSample';
import { QaSampleDetail } from '../model/QaSampleDetail';
import { QaSampleCount } from '../model/QaSampleCount';

// eslint-disable-next-line @typescript-eslint/no-var-requires
const axios = require('axios').default;

const GET_QA_SAMPLE_LIST_URL = '/api/qa-sample-compact';
const GET_QA_SAMPLE_DETAIL_URL = '/api/qa-sample';
const COUNT_QA_SAMPLE = '/api/qa-sample-count';

export async function getQaSampleList(page: number, size: number) {
    const response = await axios.get(GET_QA_SAMPLE_LIST_URL, {
        params: { page: page - 1, size },
    });
    const data: { data: CompactQaSample[]; totalCount: number } = response.data;
    return data;
}

export async function getQaSampleDetail(id: number) {
    const response = await axios.get(`${GET_QA_SAMPLE_DETAIL_URL}/${id}`);
    const data: QaSampleDetail = response.data['data'];
    return data;
}

export async function countQaSample() {
    const response = await axios.get(COUNT_QA_SAMPLE);
    const data: QaSampleCount[] = response.data['data'];
    return data;
}
