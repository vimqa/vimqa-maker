import { Card, Typography } from 'antd';

const { Title, Paragraph } = Typography;

const Welcome = () => (
    <Card>
        <Title>NguyenLab QA Dataset Builder</Title>
        <Paragraph>Welcome QA Dataset Builder</Paragraph>
        <Title level={2}>Instructions</Title>
        <Paragraph>
            <ul>
                <li>Choose your working user on the top left corner</li>
                <li>Choose Manage data on the left to view dataset</li>
                <li>Choose Make data on the left to create data</li>
            </ul>
        </Paragraph>
    </Card>
);

export default Welcome;
