interface QaSampleParagraph {
    title: string;
    sentences: string[];
}

export interface QaSampleDetail {
    id?: number;
    answer: string;
    question: string;
    firstParagraph: QaSampleParagraph;
    secondParagraph: QaSampleParagraph;
    firstSupportingFactIndex: number[];
    secondSupportingFactIndex: number[];
}
