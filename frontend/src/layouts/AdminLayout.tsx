import { Col, Layout, Menu, Row, Select, Space } from 'antd';
import { DatabaseOutlined, FileAddOutlined } from '@ant-design/icons';
import { createContext, useState } from 'react';
import styled from 'styled-components';
import { Link, Outlet } from 'react-router-dom';

const { Content, Footer, Sider, Header } = Layout;
const { Option } = Select;

const Logo = styled.div`
    height: 32px;
    margin: 16px;
    text-align: center;
    align-items: center;
    line-height: 32px;
    color: #fff;
    font-weight: 800;
    font-size: large;
    letter-spacing: 0.2em;
`;

export const UserContext = createContext('unknown');

const AdminLayout = () => {
    const [collapsed, setCollapsed] = useState(false);
    const [workingUser, setWorkingUser] = useState('unknown');

    const onCollapse = (collapsed: boolean) => {
        setCollapsed(collapsed);
    };

    return (
        <UserContext.Provider value={workingUser}>
            <Layout style={{ minHeight: '100vh' }}>
                <Sider collapsible collapsed={collapsed} onCollapse={onCollapse}>
                    <Link to="/">
                        <Logo>{collapsed ? 'NL' : 'NguyenLab'}</Logo>
                    </Link>
                    <Menu theme="dark" mode="inline">
                        <Menu.Item key="1" icon={<DatabaseOutlined />}>
                            <Link to="/qa-sample">Manage data</Link>
                        </Menu.Item>
                        <Menu.Item key="2" icon={<FileAddOutlined />}>
                            <Link to="/create">Create data</Link>
                        </Menu.Item>
                    </Menu>
                </Sider>
                <Layout className="site-layout">
                    <Header style={{ background: '#fff' }}>
                        <Row justify="end">
                            <Col span={12}>
                                <Space style={{ width: '100%', justifyContent: 'flex-end' }}>
                                    Working user:
                                    <Select
                                        defaultValue="lnkhang"
                                        style={{ width: 200 }}
                                        value={workingUser}
                                        onChange={(value) => setWorkingUser(value)}
                                    >
                                        <Option value="lnkhang">lnkhang</Option>
                                        <Option value="ndhien">ndhien</Option>
                                        <Option value="nttung">nttung</Option>
                                    </Select>
                                </Space>
                            </Col>
                        </Row>
                    </Header>
                    <Content style={{ margin: '24px 16px' }}>
                        <Outlet />
                    </Content>
                    <Footer style={{ textAlign: 'center' }}>NguyenLab ©2021</Footer>
                </Layout>
            </Layout>
        </UserContext.Provider>
    );
};
export default AdminLayout;
