export interface QaSampleInfo {
    id?: number;
    answer: string;
    question: string;
    firstTitle: string;
    firstParagraph: string[];
    secondTitle: string;
    secondParagraph: string[];
    firstSupportingFactIndex: number[];
    secondSupportingFactIndex: number[];
    annotator: string;
}
