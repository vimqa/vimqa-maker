import { useEffect, useState } from 'react';
import { getQaSampleDetail } from '../../api/ManageDataApi';
import { useParams } from 'react-router-dom';
import { Card, Typography } from 'antd';
import { QaSampleDetail } from '../../model/QaSampleDetail';

const { Title, Paragraph, Text } = Typography;

const DEFAULT_SAMPLE_DETAIL = {
    id: 0,
    answer: 'Loading',
    question: 'Loading',
    firstParagraph: {
        title: 'Loading',
        sentences: [],
    },
    secondParagraph: {
        title: 'Loading',
        sentences: [],
    },
    firstSupportingFactIndex: [],
    secondSupportingFactIndex: [],
};

const QaParagraphView = ({
    title,
    sentences,
    factIndex,
}: {
    title: string;
    sentences: string[];
    factIndex: number[];
}) => (
    <div>
        <Title level={5}>Paragraph B: {title}</Title>
        <Paragraph>
            {sentences.map((sentence, index) =>
                factIndex.includes(index) ? (
                    <Text mark key={index}>
                        {sentence}
                    </Text>
                ) : (
                    <Text key={index}>{sentence}</Text>
                ),
            )}
        </Paragraph>
    </div>
);

const QaSampleDetailView = () => {
    const { qaSampleId } = useParams();
    const [qaSampleDetail, setQaSampleDetail] = useState<QaSampleDetail>(DEFAULT_SAMPLE_DETAIL);
    useEffect(() => {
        async function fetchDetail() {
            if (typeof qaSampleId === 'string') {
                const qaSample: QaSampleDetail = await getQaSampleDetail(parseInt(qaSampleId));
                console.log(qaSample);

                setQaSampleDetail(qaSample);
            } else {
                console.log('qaSampleId is not a string');
            }
        }
        fetchDetail();
    }, []);
    const {
        id,
        answer,
        question,
        firstParagraph,
        secondParagraph,
        firstSupportingFactIndex,
        secondSupportingFactIndex,
    } = qaSampleDetail;
    return (
        <Card>
            <Typography>
                <Title level={3}>QA Sample {id}</Title>
                <QaParagraphView
                    title={firstParagraph.title}
                    sentences={firstParagraph.sentences}
                    factIndex={firstSupportingFactIndex}
                />
                <QaParagraphView
                    title={secondParagraph.title}
                    sentences={secondParagraph.sentences}
                    factIndex={secondSupportingFactIndex}
                />
                <Paragraph>
                    <Text strong>Question:</Text> {question}
                </Paragraph>
                <Paragraph>
                    <Text strong>Answer:</Text> {answer}
                </Paragraph>
            </Typography>
        </Card>
    );
};

export default QaSampleDetailView;
