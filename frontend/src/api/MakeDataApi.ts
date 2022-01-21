import { SupportParagraphInfo } from '../model/SupportParagraphInfo';
import { QaSampleInfo } from '../model/QaSampleInfo';
import { CreateQaSampleResponse } from '../model/CreateQaSampleResponse';

// eslint-disable-next-line @typescript-eslint/no-var-requires
const axios = require('axios').default;

const SAMPLE_PARAGRAPH_URL = '/api/sample/support-paragraph';
const CREATE_QA_SAMPLE_URL = '/api/qa-sample';

export async function getSampleSupportParagraph() {
    const response = await axios.get(SAMPLE_PARAGRAPH_URL);
    const resData: SupportParagraphInfo = response.data;
    return resData;
}

export async function postQaSample(qaSample: QaSampleInfo): Promise<CreateQaSampleResponse> {
    const response = await axios.post(CREATE_QA_SAMPLE_URL, qaSample);
    return response.data;
}
