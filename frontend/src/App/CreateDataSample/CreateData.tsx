import { Button, Card, Col, Form, Input, notification, Row, Steps } from 'antd';
import { useContext, useEffect, useState } from 'react';
import SupportParagraph from './SupportParagraph';
import styled from 'styled-components';
import { CheckOutlined, ReloadOutlined } from '@ant-design/icons';
import { getSampleSupportParagraph, postQaSample } from '../../api/MakeDataApi';
import { SupportParagraphInfo } from '../../model/SupportParagraphInfo';
import { QaSampleInfo } from '../../model/QaSampleInfo';
import { UserContext } from '../../layouts/AdminLayout';

const { Step } = Steps;

const MarginBottomDiv = styled.div`
    margin-bottom: 16px;
`;

const steps = ['Question', 'Answer', 'Supporting Facts', 'Finish'];

interface FormValues {
    question: string;
    answer: string;
    factsA?: boolean[];
    factsB?: boolean[];
}

const oneHotToIndices = (a: boolean[] | null | undefined) => {
    if (!a) {
        return [];
    }
    const ret: number[] = [];
    a.forEach((value, index) => {
        if (value) {
            ret.push(index);
        }
    });
    return ret;
};

const CreateData = () => {
    const [form] = Form.useForm<FormValues>();
    const [disabledSubmit, setDisabledSubmit] = useState(true);
    const [isSampleLoading, setIsSampleLoading] = useState(false);
    const [isSubmitLoading, setIsSubmitLoading] = useState(false);
    const [currentStep, setCurrentStep] = useState(0);
    const [supportParagraph, setSupportParagraph] = useState<SupportParagraphInfo>({
        titleA: 'Loading',
        paragraphA: [],
        titleB: 'Loading',
        paragraphB: [],
    });
    const workingUser = useContext(UserContext);
    async function onSubmitSample(values: FormValues) {
        try {
            if (workingUser === 'unknown') {
                notification['warning']({
                    message: 'Unknown working user',
                    description: 'Please choose a working user',
                    placement: 'bottomRight',
                });
                return;
            }
            const qaSample: QaSampleInfo = {
                answer: values.answer,
                question: values.question,
                firstTitle: supportParagraph.titleA,
                firstParagraph: supportParagraph.paragraphA,
                secondTitle: supportParagraph.titleB,
                secondParagraph: supportParagraph.paragraphB,
                firstSupportingFactIndex: oneHotToIndices(values.factsA),
                secondSupportingFactIndex: oneHotToIndices(values.factsB),
                annotator: workingUser,
            };
            setIsSubmitLoading(true);
            setDisabledSubmit(true);
            const response = await postQaSample(qaSample);

            notification['success']({
                message: 'Sample saved successfully',
                description: `Sample ID: ${response.qaSample.id}`,
            });
            fetchSupportParagraphs();
        } catch (ex) {
            notification['error']({
                message: 'Error saving sample',
                description: (ex as Error).message,
            });
        } finally {
            setIsSubmitLoading(false);
            form.resetFields();
            handleFormChange();
        }
    }

    async function fetchSupportParagraphs() {
        setIsSampleLoading(true);
        try {
            const paragraphInfos = await getSampleSupportParagraph();
            setSupportParagraph(paragraphInfos);
        } finally {
            setIsSampleLoading(false);
        }
    }

    useEffect(() => {
        fetchSupportParagraphs();
    }, []);

    const inferStepFromValues = (values: FormValues): number => {
        if (values.question) {
            if (values.answer) {
                const emptyA = values.factsA ? values.factsA.every((v) => !v) : true;
                const emptyB = values.factsB ? values.factsB.every((v) => !v) : true;
                if (!emptyA && !emptyB) {
                    return 3;
                } else {
                    return 2;
                }
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    };

    const handleFormChange = () => {
        const newCurrentStep = inferStepFromValues(form.getFieldsValue());
        setCurrentStep(newCurrentStep);
        setDisabledSubmit(newCurrentStep !== steps.length - 1);
    };

    return (
        <Card>
            <MarginBottomDiv>
                <Row align="middle">
                    <Col span={6}>
                        <Button
                            type="primary"
                            icon={<ReloadOutlined />}
                            loading={isSampleLoading}
                            disabled={isSampleLoading}
                            onClick={() => fetchSupportParagraphs()}
                        >
                            New Paragraphs
                        </Button>
                    </Col>
                    <Col span={18}>
                        <Steps size="small" current={currentStep}>
                            {steps.map((step) => (
                                <Step key={step} title={step} />
                            ))}
                        </Steps>
                    </Col>
                </Row>
            </MarginBottomDiv>

            <Form<FormValues>
                name="qa-form"
                form={form}
                onFieldsChange={handleFormChange}
                initialValues={{
                    question: '',
                    answer: '',
                    factsA: null,
                    factsB: null,
                }}
                onFinish={onSubmitSample}
                autoComplete="off"
            >
                <Form.Item name="factsA" rules={[{ required: true }]} hidden />
                <Form.Item name="factsB" rules={[{ required: true }]} hidden />
                <MarginBottomDiv>
                    <SupportParagraph
                        name="Paragraph A"
                        title={supportParagraph.titleA}
                        sentences={supportParagraph.paragraphA}
                        onChange={(values) => {
                            form.setFieldsValue({ factsA: values });
                            handleFormChange();
                        }}
                    />
                </MarginBottomDiv>
                <MarginBottomDiv>
                    <SupportParagraph
                        name="Paragraph B"
                        title={supportParagraph.titleB}
                        sentences={supportParagraph.paragraphB}
                        onChange={(values) => {
                            form.setFieldsValue({ factsB: values });
                            handleFormChange();
                        }}
                    />
                </MarginBottomDiv>

                <Form.Item name="question" rules={[{ required: true, message: 'Enter the question' }]}>
                    <Input addonBefore="Question" placeholder="Enter your question" />
                </Form.Item>
                <Form.Item name="answer" rules={[{ required: true, message: 'Enter the answer' }]}>
                    <Input addonBefore="Answer" placeholder="Enter your answer" />
                </Form.Item>
                <Button
                    size="large"
                    block
                    type="primary"
                    htmlType="submit"
                    loading={isSubmitLoading}
                    disabled={disabledSubmit}
                    icon={<CheckOutlined />}
                >
                    Submit
                </Button>
            </Form>
        </Card>
    );
};

export default CreateData;
