// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract VulnerableBank {
    mapping(address => uint256) public balances;

    function deposit() external payable {
        balances[msg.sender] += msg.value;
    }

    function withdrawAll() external {
        uint256 bal = balances[msg.sender];
        require(bal > 0, "no balance");
        (bool sent, ) = msg.sender.call{value: bal}("");
        require(sent, "failed");
        balances[msg.sender] = 0;
    }
}
