import { Card, Tag, Typography } from 'antd';
import { MouseEventHandler, useEffect, useState } from 'react';
import styled from 'styled-components';

const { Text } = Typography;

interface SupportParagraphProps {
    name: string;
    title: string;
    sentences: string[];
    className?: string;
    onChange?: (values: boolean[]) => void;
}

interface SentenceWithTagProps {
    sentence: string;
    index: number;
    onClick?: MouseEventHandler<HTMLButtonElement>;
    active: boolean;
    key?: string | number;
}

const StyledTag = styled(Tag)<{ marginLeft: boolean }>`
    margin-left: ${(props) => (props.marginLeft ? '4px' : '0px')};
    margin-right: 4px;
`;

const HoverSpan = styled.span`
    &:hover {
        cursor: pointer;
    }
`;

const SentenceWithTag = ({ sentence, index, onClick, active }: SentenceWithTagProps) => (
    <HoverSpan onClick={onClick}>
        <StyledTag marginLeft={index !== 0} color={active ? '#1890ff' : 'default'}>
            {index}
        </StyledTag>
        <span style={{ color: active ? '#1890ff' : undefined }}>{sentence}</span>
    </HoverSpan>
);

const SupportParagraph = ({ sentences, className, onChange, title }: SupportParagraphProps) => {
    const [supportingFactIndices, setSupportingFactIndices] = useState(
        new Array<boolean>(sentences.length).fill(false),
    );
    useEffect(() => {
        setSupportingFactIndices(new Array<boolean>(sentences.length).fill(false));
    }, [sentences, title]);
    return (
        <div className={className}>
            <Card size="small" title={<Text copyable>{title}</Text>}>
                <p>
                    {sentences.map((sent, idx) => (
                        <SentenceWithTag
                            key={idx}
                            index={idx}
                            sentence={sent}
                            active={supportingFactIndices[idx]}
                            onClick={() => {
                                const newFactIdx = [...supportingFactIndices];
                                newFactIdx[idx] = !newFactIdx[idx];
                                setSupportingFactIndices(newFactIdx);
                                if (onChange) {
                                    onChange(newFactIdx);
                                }
                            }}
                        />
                    ))}
                </p>
            </Card>
        </div>
    );
};

export default SupportParagraph;
