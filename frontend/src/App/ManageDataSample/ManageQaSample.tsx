import { Avatar, Card, Col, Row, Table, Typography } from 'antd';
import { useEffect, useState } from 'react';
import { countQaSample, getQaSampleList } from '../../api/ManageDataApi';
import { Link } from 'react-router-dom';
import { CompactQaSample } from '../../model/CompactQaSample';
import { BarChartOutlined } from '@ant-design/icons';
import { QaSampleCount } from '../../model/QaSampleCount';
import { TablePaginationConfig } from 'antd/lib/table/interface';

const { Title } = Typography;
const { Meta } = Card;

const columns = [
    {
        title: 'ID',
        dataIndex: 'id',
        key: 'id',
        width: '10%',
        render: (id: string) => <Link to={`/qa-sample/${id}`}>{id}</Link>,
    },
    {
        title: 'Question',
        dataIndex: 'question',
        key: 'question',
        ellipsis: true,
        render: (text: string, { id }: CompactQaSample) => <Link to={`/qa-sample/${id}`}>{text}</Link>,
    },
    {
        title: 'Answer',
        dataIndex: 'answer',
        key: 'answer',
        ellipsis: true,
    },
    {
        title: 'First Title',
        dataIndex: 'firstTitle',
        key: 'firstTitle',
        ellipsis: true,
    },
    {
        title: 'Second Title',
        dataIndex: 'secondTitle',
        key: 'secondTitle',
        ellipsis: true,
    },
    {
        title: 'Annotator',
        dataIndex: 'annotator',
        key: 'annotator',
    },
];

const annotatorColor = ['#fa8c16', '#52c41a', '#722ed1', '#1890ff'];
const DEFAULT_PAGE_SIZE = 10;

const ManageQaSample = () => {
    const [qaSampleList, setQaSampleList] = useState<CompactQaSample[]>([]);
    const [totalCount, setTotalCount] = useState(0);
    const [countByAnnotator, setCountByAnnotator] = useState<QaSampleCount[]>([]);
    const [pagination, setPagination] = useState<TablePaginationConfig>({
        showSizeChanger: true,
        current: 1,
        pageSize: DEFAULT_PAGE_SIZE,
    });
    const [tableLoading, setTableLoading] = useState(false);

    async function fetchQaSampleData(pagination: TablePaginationConfig) {
        setTableLoading(true);
        const page = pagination.current ? pagination.current : 1;
        const size = pagination.pageSize ? pagination.pageSize : DEFAULT_PAGE_SIZE;
        const { data, totalCount } = await getQaSampleList(page, size);
        setQaSampleList(data);
        setTableLoading(false);
        setPagination({ ...pagination, current: page, pageSize: size, total: totalCount });
    }

    async function fetchQaCount() {
        const countInfo: QaSampleCount[] = await countQaSample();
        const totalCountElement = countInfo.find((value) => value.annotator === 'total');
        if (totalCountElement) {
            setTotalCount(totalCountElement.sampleCount);
            countInfo.splice(countInfo.indexOf(totalCountElement), 1);
        }
        setCountByAnnotator(countInfo);
    }

    useEffect(() => {
        fetchQaSampleData(pagination);
        fetchQaCount();
    }, []);

    return (
        <div>
            <Card style={{ marginBottom: 16 }}>
                <Title level={3}>Overall</Title>
                <Card>
                    <Meta
                        avatar={<Avatar size={64} style={{ backgroundColor: '#1890ff' }} icon={<BarChartOutlined />} />}
                        title={`${totalCount} samples`}
                        description="Total number of samples"
                    />
                </Card>
            </Card>
            <Card style={{ marginBottom: 16 }}>
                <Title level={3}>Dataset detail</Title>
                <Table
                    columns={columns}
                    dataSource={qaSampleList}
                    rowKey="id"
                    pagination={pagination}
                    loading={tableLoading}
                    onChange={fetchQaSampleData}
                />
            </Card>
            <Card>
                <Title level={3}>Progress</Title>
                <Row gutter={[16, 16]}>
                    {countByAnnotator.map((value, index) => (
                        <Col span={8} key={index}>
                            <Card>
                                <Meta
                                    avatar={
                                        <Avatar
                                            size={64}
                                            style={{
                                                backgroundColor: annotatorColor[index % annotatorColor.length],
                                            }}
                                        >
                                            {value.annotator}
                                        </Avatar>
                                    }
                                    title={`${value.sampleCount} samples`}
                                    description={`${value.annotator} makes ${value.sampleCount} samples`}
                                />
                            </Card>
                        </Col>
                    ))}
                </Row>
            </Card>
        </div>
    );
};

export default ManageQaSample;
